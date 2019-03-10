package com.example

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable.Specification

object UserDataDBServiceSpec extends Specification {

  sequential

  "Member should create a new record" in new AutoRollback {
    //val before = Member.count()
    //Member.create(3, "Chris")
   // Member.count() must_==(before + 1)
  }

  "Member should ... " in new AutoRollbackWithFixture {

  }

}

trait AutoRollbackWithFixture extends AutoRollback {
  // override def db = NamedDB('db2).toDB
  override def fixture(implicit session: DBSession) {
    sql"insert into members values (1, ${"Alice"}, current_timestamp)".update.apply()
    sql"insert into members values (2, ${"Bob"}, current_timestamp)".update.apply()
  }
}


