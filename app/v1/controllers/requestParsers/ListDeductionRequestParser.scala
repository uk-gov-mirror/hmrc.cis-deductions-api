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

package v1.controllers.requestParsers

import javax.inject.Inject
import uk.gov.hmrc.domain.Nino
import v1.controllers.requestParsers.validators.ListDeductionsValidator
import v1.models.request.{ListDeductionsRawData, ListDeductionsRequest}

class ListDeductionRequestParser @Inject()(val validator: ListDeductionsValidator)
  extends RequestParser[ListDeductionsRawData, ListDeductionsRequest] {

  override protected def requestFor(data: ListDeductionsRawData): ListDeductionsRequest = {
    ListDeductionsRequest(Nino(data.nino), data.fromDate, data.toDate, data.source)
  }
}