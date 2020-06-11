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

package v1.fixtures

import v1.models.hateoas.HateoasWrapper
import v1.models.responseData.RetrieveResponseModel.{amendCISDeduction, createCISDeduction, deleteCISDeduction, retrieveCISDeduction}
import v1.models.responseData.{CisDeductions, PeriodData, RetrieveResponseModel}

object RetrieveModels {

  val cisDeductions: CisDeductions = CisDeductions(
    fromDate = "2019-04-06",
    toDate = "2020-04-05",
    contractorName = Some("contractor Name"),
    employerRef = "123/AA12345",
    totalDeductionAmount = Some(3543.55),
    totalCostOfMaterials = Some(6644.67),
    totalGrossAmountPaid = Some(3424.12),
    Seq(
      PeriodData(
        deductionFromDate = "2019-06-06",
        deductionToDate = "2019-07-05",
        deductionAmount = Some(355.00),
        costOfMaterials = Some(35.00),
        grossAmountPaid = 1457.00,
        submissionDate = "2020-05-11T16:38:57.489Z",
        submissionId = Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"),
        source = Some("customer")),
      PeriodData(
        deductionFromDate = "2019-07-06",
        deductionToDate = "2019-08-05",
        deductionAmount = Some(355.00),
        costOfMaterials = Some(35.00),
        grossAmountPaid = 1457.00,
        submissionDate = "2020-05-11T16:38:57.489Z",
        submissionId = Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"),
        source = Some("customer")),
    )
  )

  val cisDeductionsNoId: CisDeductions = CisDeductions(
    fromDate = "2019-04-06",
    toDate = "2020-04-05",
    contractorName = Some("contractor Name"),
    employerRef = "123/AA12345",
    totalDeductionAmount = Some(3543.55),
    totalCostOfMaterials = Some(6644.67),
    totalGrossAmountPaid = Some(3424.12),
    Seq(
      PeriodData(
        deductionFromDate = "2019-06-06",
        deductionToDate = "2019-07-05",
        deductionAmount = Some(355.00),
        costOfMaterials = Some(35.00),
        grossAmountPaid = 1457.00,
        submissionDate = "2020-05-11T16:38:57.489Z",
        submissionId = None,
        source = Some("contractor")),
      PeriodData(
        deductionFromDate = "2019-07-06",
        deductionToDate = "2019-08-05",
        deductionAmount = Some(355.00),
        costOfMaterials = Some(35.00),
        grossAmountPaid = 1457.00,
        submissionDate = "2020-05-11T16:38:57.489Z",
        submissionId = None,
        source = Some("contractor")),
    )
  )

  val cisDeductionsMissingOptional: CisDeductions = CisDeductions(
    fromDate = "2019-04-06",
    toDate = "2020-04-05",
    contractorName = Some("contractor Name"),
    employerRef = "123/AA12345",
    totalDeductionAmount = Some(3543.55),
    totalCostOfMaterials = Some(6644.67),
    totalGrossAmountPaid = Some(3424.12),
    Seq(
      PeriodData(
        deductionFromDate = "2019-06-06",
        deductionToDate = "2019-07-05",
        deductionAmount = None,
        costOfMaterials = Some(35.00),
        grossAmountPaid = 1457.00,
        submissionDate = "2020-05-11T16:38:57.489Z",
        submissionId = Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"),
        source = Some("contractor")),
      PeriodData(
        deductionFromDate = "2019-07-06",
        deductionToDate = "2019-08-05",
        deductionAmount = Some(355.00),
        costOfMaterials = Some(35.00),
        grossAmountPaid = 1457.00,
        submissionDate = "2020-05-11T16:38:57.489Z",
        submissionId = Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"),
        source = Some("contractor")),
    )
  )

  val multipleDeductionsModel = RetrieveResponseModel(
    totalDeductionAmount = 12345.56,
    totalCostOfMaterials = 234234.33,
    totalGrossAmountPaid = 2342424.56,
    Seq(
      CisDeductions(
        "2019-04-06",
        "2020-04-05",
        Some("Bovis"),
        "BV40092",
        Some(3543.55),
        Some(6644.67),
        Some(3424.12),
        Seq(
          PeriodData("2019-04-06", "2019-05-05", Some(355.11), Some(35.11), 1457.11, "2020-05-11T16:38:57.489Z",
            Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"), Some("contractor")),
          PeriodData("2019-05-06", "2019-06-05", Some(355.11), Some(35.11), 1457.11, "2020-05-11T16:38:57.489Z",
            Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"), Some("contractor")),
          PeriodData("2019-06-06", "2019-07-05", Some(355.11), Some(35.11), 1457.11, "2020-05-11T16:38:57.489Z",
            Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"), Some("customer")),
          PeriodData("2019-07-06", "2019-08-05", Some(355.11), Some(35.11), 1457.11, "2020-05-11T16:38:57.489Z",
            Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"), Some("customer"))
        )
      ),
      CisDeductions(
        "2019-04-06",
        "2020-04-05",
        Some("Taylor Wimpy"),
        "TW44355",
        Some(3543.55),
        Some(6644.67),
        Some(3424.12),
        Seq(
          PeriodData("2019-07-06", "2019-08-05", Some(60.11), Some(35.11), 1457.11, "2020-05-11T16:38:57.489Z",
            Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"), Some("contractor")),
          PeriodData("2019-09-06", "2019-10-05", Some(60.11), Some(35.11), 1457.11, "2020-05-11T16:38:57.489Z",
            Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"), Some("contractor"))
        )
      )
    )
  )

  val singleDeductionModel = RetrieveResponseModel(
    totalDeductionAmount = 12345.56,
    totalCostOfMaterials = 234234.33,
    totalGrossAmountPaid = 2342424.56,
    Seq(
      CisDeductions(
        "2019-04-06",
        "2020-04-05",
        Some("contractor Name"),
        "123/AA12345",
        Some(3543.55),
        Some(6644.67),
        Some(3424.12),
        Seq(
          PeriodData("2019-06-06", "2019-07-05", Some(355.00), Some(35.00), 1457.00, "2020-05-11T16:38:57.489Z",
            Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"), Some("customer")),
          PeriodData("2019-07-06", "2019-08-05", Some(355.00), Some(35.00), 1457.00, "2020-05-11T16:38:57.489Z",
            Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"), Some("customer"))
        )
      )
    )
  )

  val retrieveCisDeductionsModel =
    RetrieveResponseModel(
      totalDeductionAmount = 12345.56,
      totalCostOfMaterials = 234234.33,
      totalGrossAmountPaid = 2342424.56,
      Seq(
        CisDeductions(
          "2019-04-06",
          "2020-04-05",
          Some("Bovis"),
          "BV40092",
          Some(3543.55),
          Some(6644.67),
          Some(3424.12),
        Seq(
          PeriodData(
            deductionFromDate = "2019-06-06",
            deductionToDate = "2019-07-05",
            deductionAmount = Some(355.00),
            costOfMaterials = Some(35.00),
            grossAmountPaid = 1457.00,
            submissionDate = "2020-05-11T16:38:57.489Z",
            submissionId = Some("4557ecb5-fd32-48cc-81f5-e6acd1099f3c"),
            source = Some("customer")
          )
        )
      )
      )
    )

  val response: RetrieveResponseModel[CisDeductions] =
    RetrieveResponseModel(
      totalDeductionAmount = 12345.56,
      totalCostOfMaterials = 234234.33,
      totalGrossAmountPaid = 2342424.56,
      Seq(cisDeductions
      )
    )

  val responseNoId: RetrieveResponseModel[CisDeductions] =
    RetrieveResponseModel(
      totalDeductionAmount = 12345.56,
      totalCostOfMaterials = 234234.33,
      totalGrossAmountPaid = 2342424.56,
      Seq(cisDeductionsNoId
      )
    )
}
