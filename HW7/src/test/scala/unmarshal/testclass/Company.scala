package unmarshal.testclass

import data.{CompanyEmployee, Employee}

case class Company(
  legalName: String,
  accountNumber: String,
  director: Employee,
  bookkeeper: Option[Employee],
  active: Boolean,
  staff: CompanyEmployee
)
