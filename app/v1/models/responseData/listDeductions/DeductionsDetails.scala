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

package v1.models.responseData.listDeductions

import play.api.libs.json._
import play.api.libs.functional.syntax._


case class DeductionsDetails(submissionId: Option[String],
                             fromDate: String,
                             toDate: String,
                             contractorName: String,
                             employerRef: String,
                             periodData: Seq[PeriodDeductions]
                            )

object DeductionsDetails {
//  implicit val reads: Reads[DeductionsDetails] = Json.reads[DeductionsDetails]

  implicit val reads: Reads[DeductionsDetails] = (
    (JsPath \ "submissionId").readNullable[String] and
      (JsPath \ "fromDate").read[String] and
      (JsPath \ "toDate").read[String] and
      (JsPath \ "contractorName").read[String] and
      (JsPath \ "employerRef").read[String] and
      (JsPath \ "periodData").read[Seq[PeriodDeductions]]
    ) (DeductionsDetails.apply _)

  implicit val writes: OWrites[DeductionsDetails] = Json.writes[DeductionsDetails]
}
