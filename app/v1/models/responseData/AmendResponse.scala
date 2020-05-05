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

package v1.models.responseData

import config.AppConfig
import play.api.libs.json.{Json, OFormat}
import v1.hateoas.{HateoasLinks, HateoasLinksFactory}
import v1.models.hateoas.{HateoasData, Link}

case class AmendResponse(id: String)

object AmendResponse extends HateoasLinks {
  implicit val format: OFormat[AmendResponse] = Json.format[AmendResponse]

  implicit object AmendLinksFactory extends HateoasLinksFactory[AmendResponse, AmendHateoasData] {
    override def links(appConfig: AppConfig, data: AmendHateoasData): Seq[Link] = {
      import data._
      Seq(listLink(appConfig, nino))
    }
  }
}

case class AmendHateoasData(nino: String) extends HateoasData

