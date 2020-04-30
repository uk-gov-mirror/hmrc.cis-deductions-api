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
import play.api.libs.json.JsValue
import v1.models.hateoas.Link
import v1.models.hateoas.Method._
import v1.models.hateoas.RelType._

trait HateoasLinks {

  private def baseUri(appConfig: AppConfig, nino: String) =
    s"/${appConfig.apiGatewayContext}/$nino"

  private def createUri(appConfig: AppConfig, nino: String): String =
    baseUri(appConfig, nino) + s"/amendments"
  private def deleteUri(appConfig: AppConfig, nino: String, id: String): String =
    baseUri(appConfig, nino) + s"/amendments/$id"
  private def amendUri(appConfig: AppConfig, nino: String, id: Option[String]): String =
    baseUri(appConfig, nino) + s"/amendments/$id"
  private def listUri(appConfig: AppConfig, nino: String, fromDate: Option[String], toDate: Option[String], source: Option[String]): String = {
    val sourceStr = source.getOrElse()
    val sourceParam = if (sourceStr == "None") "" else s"&source=$sourceStr"
    baseUri(appConfig, nino) + s"/current-position?fromDate=${fromDate.getOrElse()}" +
      s"&toDate=${toDate.getOrElse()}$sourceParam"
  }

  //API resource links
  //L1
  def createCISDeduction(appConfig: AppConfig, nino: String, isSelf: Boolean) : Link =
    Link (
      href = createUri(appConfig, nino),
      method = POST,
      rel = if(isSelf) SELF else CREATE_CIS)

  //L2
  def deleteCISDeduction(appConfig: AppConfig, nino: String, id: String, isSelf: Boolean):
  Link =
    Link (
      href = deleteUri(appConfig, nino, id),
      method = DELETE,
      rel = if(isSelf) SELF else DELETE_CIS)

  //L3
  def amendCISDeduction(appConfig: AppConfig, nino:String, id: Option[String], body: JsValue, isSelf: Boolean):
  Link =
    Link (
      href = amendUri(appConfig, nino, id),
      method = PUT,
      rel = if(isSelf) SELF else AMEND_CIS)

  //L4
  def listCISDeduction(appConfig: AppConfig, nino: String, fromDate: Option[String], toDate: Option[String], source: Option[String], isSelf: Boolean):
  Link =
    Link (
      href = listUri(appConfig, nino, fromDate, toDate, source),
      method = GET,
      rel = if(isSelf) SELF else LIST_CIS)
}
