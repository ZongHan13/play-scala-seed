package controllers

import javax.inject._
import play.api._
import play.api.mvc._


@Singleton 
class TaskList1 @Inject()(controllerComponents: ControllerComponents) extends AbstractController(controllerComponents){
  
  def login = Action {
    Ok{views.html.login1()}
  }
  
  def valdiateLoginGet(username: String, password:String) = Action {
    Ok(s"$username logged in with $password")
  }
  def validateLoginPost = Action { request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map{ args => 
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.validateUser(username, password)) {
        Redirect(routes.TaskList1.taskList)
      } else {
      Redirect(routes.TaskList1.login)
    }
    }.getOrElse(Redirect(routes.TaskList1.login))
  }
  
  def taskList = Action {
    val tasks = List("task1", "task2", "task3","sleep", "eat")
    Ok(views.html.taskList1(tasks))
  }
}
