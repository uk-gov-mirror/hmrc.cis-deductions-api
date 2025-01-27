/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v1.controllers

import mocks.MockAppConfig
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import v1.fixtures.CreateRequestFixtures._
import v1.mocks.MockIdGenerator
import v1.mocks.hateoas.MockHateoasFactory
import v1.mocks.requestParsers.MockCreateRequestParser
import v1.mocks.services._
import v1.models.audit._
import v1.models.errors._
import v1.models.hateoas.Method._
import v1.models.hateoas.{HateoasWrapper, Link}
import v1.models.outcomes.ResponseWrapper
import v1.models.request.create.{CreateRawData, CreateRequestData}
import v1.models.response.create.{CreateHateoasData, CreateResponseModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateControllerSpec
  extends ControllerBaseSpec
    with MockEnrolmentsAuthService
    with MockMtdIdLookupService
    with MockCreateRequestParser
    with MockCreateService
    with MockHateoasFactory
    with MockAppConfig
    with MockAuditService
    with MockIdGenerator {

  trait Test {
    val hc: HeaderCarrier = HeaderCarrier()

    val controller = new CreateController(
      authService = mockEnrolmentsAuthService,
      lookupService = mockMtdIdLookupService,
      requestParser = mockRequestDataParser,
      service = mockService,
      hateoasFactory = mockHateoasFactory,
      auditService = mockAuditService,
      appConfig = mockAppConfig,
      cc = cc,
      idGenerator = mockIdGenerator
    )

    MockIdGenerator.getCorrelationId.returns(correlationId)
    MockedMtdIdLookupService.lookup(nino).returns(Future.successful(Right("test-mtd-id")))
    MockedEnrolmentsAuthService.authoriseUser()
  }

  private val nino = "AA123456A"
  private val correlationId = "X-123"

  private val responseId = "S4636A77V5KB8625U"

  private val rawCreateRequest = CreateRawData(nino, requestJson)
  private val createRequest = CreateRequestData(Nino(nino), requestObj)

  private val rawMissingOptionalCreateRequest = CreateRawData(nino, missingOptionalRequestJson)
  private val missingOptionalCreateRequest = CreateRequestData(Nino(nino), missingOptionalRequestObj)

  val response = CreateResponseModel(responseId)

  val testHatoeasLinks: Seq[Link] = Seq(
    Link(
      href = s"/individuals/deductions/cis/$nino/current-position",
      rel = "retrieve-cis-deductions-for-subcontractor",
      method = GET
    )
  )

  private val parsedHateoas = Json.parse(hateoasResponse(nino, responseId))

  def event(auditResponse: AuditResponse, requestBody: Option[JsValue]): AuditEvent[GenericAuditDetail] =
    AuditEvent(
      auditType = "CreateCisDeductionsForSubcontractor",
      transactionName = "create-cis-deductions-for-subcontractor",
      detail = GenericAuditDetail(
        userType = "Individual",
        agentReferenceNumber = None,
        nino,
        None,
        `X-CorrelationId` = correlationId,
        requestBody,
        auditResponse
      )
    )

  "createRequest" should {
    "return a successful response with status 200 (OK)" when {
      "a valid request is supplied for a cis post request" in new Test {

        MockCreateRequestDataParser
          .parse(rawCreateRequest)
          .returns(Right(createRequest))

        MockCreateService
          .submitCreateRequest(createRequest)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, response))))

        MockHateoasFactory
          .wrap(response, CreateHateoasData(nino, createRequest))
          .returns(HateoasWrapper(response, testHatoeasLinks))

        val result: Future[Result] = controller.createRequest(nino)(fakePostRequest(Json.toJson(requestJson)))

        status(result) shouldBe OK
        contentAsJson(result) shouldBe parsedHateoas
        header("X-CorrelationId", result) shouldBe Some(correlationId)

        val auditResponse: AuditResponse = AuditResponse(OK, None, Some(parsedHateoas))
        MockedAuditService.verifyAuditEvent(event(auditResponse, Some(requestJson))).once
      }

      "a valid request is supplied when an optional field is missing" in new Test {

        MockCreateRequestDataParser
          .parse(rawMissingOptionalCreateRequest)
          .returns(Right(missingOptionalCreateRequest))

        MockCreateService
          .submitCreateRequest(missingOptionalCreateRequest)
          .returns(Future.successful((Right(ResponseWrapper(correlationId, response)))))

        MockHateoasFactory
          .wrap(response, CreateHateoasData(nino, missingOptionalCreateRequest))
          .returns(HateoasWrapper(response, testHatoeasLinks))

        val result: Future[Result] = controller.createRequest(nino)(fakePostRequest(Json.toJson(missingOptionalRequestJson)))

        status(result) shouldBe OK
        contentAsJson(result) shouldBe parsedHateoas
        header("X-CorrelationId", result) shouldBe Some(correlationId)

        val auditResponse: AuditResponse = AuditResponse(OK, None, Some(parsedHateoas))
        MockedAuditService.verifyAuditEvent(event(auditResponse, Some(missingOptionalRequestJson))).once
      }
    }

    "return errors as per the spec" when {
      def errorsFromParserTester(error: MtdError, expectedStatus: Int): Unit = {
        s"a ${error.code} error is returned from the parser" in new Test {

          MockCreateRequestDataParser
            .parse(rawCreateRequest)
            .returns(Left(ErrorWrapper(correlationId, error)))

          val result: Future[Result] = controller.createRequest(nino)(fakePostRequest(requestJson))

          status(result) shouldBe expectedStatus
          contentAsJson(result) shouldBe Json.toJson(error)
          header("X-CorrelationId", result) shouldBe Some(correlationId)

          val auditResponse: AuditResponse = AuditResponse(expectedStatus, Some(Seq(AuditError(error.code))), None)
          MockedAuditService.verifyAuditEvent(event(auditResponse, Some(requestJson))).once
        }
      }

      val input = Seq(
        (BadRequestError, BAD_REQUEST),
        (NinoFormatError, BAD_REQUEST),
        (EmployerRefFormatError, BAD_REQUEST),
        (DeductionFromDateFormatError, BAD_REQUEST),
        (DeductionToDateFormatError, BAD_REQUEST),
        (FromDateFormatError, BAD_REQUEST),
        (ToDateFormatError, BAD_REQUEST),
        (RuleTaxYearNotSupportedError, BAD_REQUEST),
        (RuleIncorrectOrEmptyBodyError, BAD_REQUEST),
        (RuleDeductionAmountError, BAD_REQUEST),
        (RuleCostOfMaterialsError, BAD_REQUEST),
        (RuleGrossAmountError, BAD_REQUEST),
        (DownstreamError, INTERNAL_SERVER_ERROR),
      )

      input.foreach(args => (errorsFromParserTester _).tupled(args))

      "multiple parser errors occur" in new Test {
        val error = ErrorWrapper(correlationId, BadRequestError, Some(Seq(BadRequestError, NinoFormatError)))

        MockCreateRequestDataParser
          .parse(rawCreateRequest)
          .returns(Left(error))

        val result: Future[Result] = controller.createRequest(nino)(fakePostRequest(Json.toJson(requestJson)))

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(error)
        header("X-CorrelationId", result) shouldBe Some(correlationId)

        val auditResponse: AuditResponse = AuditResponse(BAD_REQUEST, Some(Seq(AuditError(BadRequestError.code), AuditError(NinoFormatError.code))), None)
        MockedAuditService.verifyAuditEvent(event(auditResponse, Some(requestJson))).once
      }

      "multiple errors occur for format errors" in new Test {
        val error = ErrorWrapper(
          correlationId,
          BadRequestError,
          Some(Seq(
            EmployerRefFormatError,
            NinoFormatError,
            BadRequestError,
            DeductionToDateFormatError,
            DeductionFromDateFormatError,
            ToDateFormatError,
            FromDateFormatError,
            RuleTaxYearNotSupportedError,
            TaxYearFormatError
          ))
        )

        MockCreateRequestDataParser
          .parse(rawCreateRequest)
          .returns(Left(error))

        val result: Future[Result] = controller.createRequest(nino)(fakePostRequest(Json.toJson(requestJson)))

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(error)
        header("X-CorrelationId", result) shouldBe Some(correlationId)

        val auditResponse: AuditResponse = AuditResponse(BAD_REQUEST, Some(
          Seq(
            AuditError(EmployerRefFormatError.code),
            AuditError(NinoFormatError.code),
            AuditError(BadRequestError.code),
            AuditError(DeductionToDateFormatError.code),
            AuditError(DeductionFromDateFormatError.code),
            AuditError(ToDateFormatError.code),
            AuditError(FromDateFormatError.code),
            AuditError(RuleTaxYearNotSupportedError.code),
            AuditError(TaxYearFormatError.code))),
          None
        )

        MockedAuditService.verifyAuditEvent(event(auditResponse, Some(requestJson))).once
      }
    }

    "return downstream errors as per the spec" when {
      def serviceErrors(mtdError: MtdError, expectedStatus: Int): Unit = {
        s"a ${mtdError.code} error is returned from the service" in new Test {

          MockCreateRequestDataParser
            .parse(rawCreateRequest)
            .returns(Right(createRequest))

          MockCreateService
            .submitCreateRequest(createRequest)
            .returns(Future.successful(Left(ErrorWrapper(correlationId, mtdError))))

          val result: Future[Result] = controller.createRequest(nino)(fakePostRequest(Json.toJson(requestJson)))

          status(result) shouldBe expectedStatus
          contentAsJson(result) shouldBe Json.toJson(mtdError)
          header("X-CorrelationId", result) shouldBe Some(correlationId)

          val auditResponse: AuditResponse = AuditResponse(expectedStatus, Some(Seq(AuditError(mtdError.code))), None)
          MockedAuditService.verifyAuditEvent(event(auditResponse, Some(requestJson))).once
        }
      }

      val input = Seq(
        (NinoFormatError, BAD_REQUEST),
        (DownstreamError, INTERNAL_SERVER_ERROR),
        (RuleIncorrectOrEmptyBodyError, BAD_REQUEST),
        (EmployerRefFormatError, BAD_REQUEST),
        (RuleUnalignedDeductionsPeriodError, FORBIDDEN),
        (RuleDeductionsDateRangeInvalidError, FORBIDDEN),
        (RuleTaxYearNotEndedError, FORBIDDEN),
        (RuleDuplicateSubmissionError, FORBIDDEN),
        (RuleDuplicatePeriodError, FORBIDDEN),
        (NotFoundError, NOT_FOUND)
      )
      input.foreach(args => (serviceErrors _).tupled(args))
    }
  }
}
