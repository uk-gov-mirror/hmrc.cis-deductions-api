package data

import play.api.libs.json.Json

object CreateDataExamples {

  val deductionsResponseBody = Json.parse(
    """
      | {
      | "id" : "someResponse"
      | }
    """.stripMargin)

  def errorBody(code: String): String =
    s"""
       |      {
       |        "code": "$code",
       |        "reason": "des message"
       |      }
    """.stripMargin

  val requestJson: String =
    s"""
       |{
       |  "fromDate": "2019-04-06" ,
       |  "toDate": "2020-04-05",
       |  "contractorName": "Bovis",
       |  "employerRef": "BV40092",
       |  "periodData": [
       |      {
       |      "deductionAmount": 355.00,
       |      "deductionFromDate": "2019-06-06",
       |      "deductionToDate": "2019-07-05",
       |      "costOfMaterials": 35.00,
       |      "grossAmountPaid": 1457.00
       |    },
       |    {
       |      "deductionAmount": 355.00,
       |      "deductionFromDate": "2019-07-06",
       |      "deductionToDate": "2019-08-05",
       |      "costOfMaterials": 35.00,
       |      "grossAmountPaid": 1457.00
       |    }
       |  ]
       |}
       |""".stripMargin

  val requestBodyJson = Json.parse(
    """
      |{
      |  "fromDate": "2019-04-06" ,
      |  "toDate": "2020-04-05",
      |  "contractorName": "Bovis",
      |  "employerRef": "BV40092",
      |  "periodData": [
      |      {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-06-06",
      |      "deductionToDate": "2019-07-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    },
      |    {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-07-06",
      |      "deductionToDate": "2019-08-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    }
      |  ]
      |}
        """.stripMargin
  )
  val requestBodyJsonErrorFromDate = Json.parse(
    """
      |{
      |  "fromDate": "04-06-2019" ,
      |  "toDate": "2020-04-05",
      |  "contractorName": "Bovis",
      |  "employerRef": "BV40092",
      |  "periodData": [
      |      {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-06-06",
      |      "deductionToDate": "2019-07-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    },
      |    {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-07-06",
      |      "deductionToDate": "2019-08-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    }
      |  ]
      |}
        """.stripMargin
  )
  val requestBodyJsonErrorToDate = Json.parse(
    """
      |{
      |  "fromDate": "2019-04-06" ,
      |  "toDate": "04-05-2020",
      |  "contractorName": "Bovis",
      |  "employerRef": "BV40092",
      |  "periodData": [
      |      {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-06-06",
      |      "deductionToDate": "2019-07-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    },
      |    {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-07-06",
      |      "deductionToDate": "2019-08-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    }
      |  ]
      |}
        """.stripMargin
  )
  val requestBodyJsonErrorDeductionFromDate = Json.parse(
    """
      |{
      |  "fromDate": "2019-04-06" ,
      |  "toDate": "2020-04-05",
      |  "contractorName": "Bovis",
      |  "employerRef": "BV40092",
      |  "periodData": [
      |      {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "06-06-2019",
      |      "deductionToDate": "2019-07-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    },
      |    {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-07-06",
      |      "deductionToDate": "2019-08-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    }
      |  ]
      |}
        """.stripMargin
  )
  val requestBodyJsonErrorDeductionToDate = Json.parse(
    """
      |{
      |  "fromDate": "2019-04-06" ,
      |  "toDate": "2020-04-05",
      |  "contractorName": "Bovis",
      |  "employerRef": "BV40092",
      |  "periodData": [
      |      {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-06-06",
      |      "deductionToDate": "07-05-2019",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    },
      |    {
      |      "deductionAmount": 355.00,
      |      "deductionFromDate": "2019-07-06",
      |      "deductionToDate": "2019-08-05",
      |      "costOfMaterials": 35.00,
      |      "grossAmountPaid": 1457.00
      |    }
      |  ]
      |}
        """.stripMargin
  )
}