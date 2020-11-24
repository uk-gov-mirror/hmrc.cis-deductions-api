/*
 * Copyright 2020 HM Revenue & Customs
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

package v1.controllers.requestParsers.validators

import v1.controllers.requestParsers.validators.validations._
import v1.models.errors._
import v1.models.request.AmendRawData
import v1.models.request.amend.{AmendRawData, AmendRequest}

class   AmendValidator extends Validator[AmendRawData] {

  private val validationSet = List(parameterFormatValidation,
    bodyFormatValidator,
    bodyRuleValidator)

  private def parameterFormatValidation: AmendRawData => List[List[MtdError]] = (data: AmendRawData) => {
    List(
      NinoValidation.validate(data.nino),
      SubmissionIdValidation.validate(data.id)
    )
  }

  private def bodyFormatValidator: AmendRawData => List[List[MtdError]] = { data =>
    List(
      JsonFormatValidation.validate[AmendRequest](data.body, RuleIncorrectOrEmptyBodyError)
    )
  }

  private def bodyRuleValidator: AmendRawData => List[List[MtdError]] = { data =>
    List(
      PeriodDataPositiveAmountValidation.validate(data.body, "deductionAmount", RuleDeductionAmountError),
      PeriodDataPositiveAmountValidation.validate(data.body, "costOfMaterials", RuleCostOfMaterialsError),
      PeriodDataPositiveAmountValidation.validate(data.body, "grossAmountPaid", RuleGrossAmountError),
      PeriodDataDeductionDateValidation.validate(data.body, "deductionFromDate", DeductionFromDateFormatError),
      PeriodDataDeductionDateValidation.validate(data.body, "deductionToDate", DeductionToDateFormatError)
    )
  }

  override def validate(data: AmendRawData): List[MtdError] = {
    run(validationSet, data).distinct
  }
}
