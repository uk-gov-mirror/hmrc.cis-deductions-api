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

package v1.hateoas

import config.AppConfig
import v1.models.hateoas.Link
import v1.models.hateoas.Method._
import v1.models.hateoas.RelType._

trait HateoasLinks {

  //Domain URIs
  private def baseUri(appConfig: AppConfig, nino: String) =
    s"/${appConfig.apiGatewayContext}/$nino"

  private def createUri(appConfig: AppConfig, nino: String): String =
    baseUri(appConfig, nino) + "/amendments"

  private def listUri(appConfig: AppConfig, nino: String): String =
    baseUri(appConfig, nino) + "/current-position"

  private def amendUri(appConfig: AppConfig, nino: String): String =
    baseUri(appConfig, nino) + "/amendments"

  //API resource links
  def createLink(appConfig: AppConfig, nino: String): Link =
    Link(href = createUri(appConfig, nino), method = GET, rel = CREATE)

  def listLink(appConfig: AppConfig, nino: String): Link =
    Link(href = listUri(appConfig, nino), method = GET, rel = LIST)

  def amendLink(appConfig: AppConfig, nino: String): Link =
    Link(href = amendUri(appConfig, nino), method = GET, rel = AMEND)

}
