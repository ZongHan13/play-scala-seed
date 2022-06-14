package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton 
class TaskList1 @Inject()(controllerComponents: ControllerComponents) extends AbstractController(controllerComponents){
  
  def login = Action {implicit request => 
    Ok{views.html.login1()}
  }
  
  def valdiateLoginGet(username: String, password:String) = Action {
    Ok(s"$username logged in with $password")
  }
  def validateLoginPost = Action {implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map{ args => 
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.validateUser(username, password)) {
        Redirect(routes.TaskList1.taskList).withSession("username" -> username).flashing("success" -> "login success")
      } else {
      Redirect(routes.TaskList1.login).flashing("error" -> "Invalid username/ password")
    }
    }.getOrElse(Redirect(routes.TaskList1.login))
  }

  def createUser = Action {implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map{ args => 
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.createUser(username, password)) {
        Redirect(routes.TaskList1.taskList).withSession("username" -> username)
      } else {
      Redirect(routes.TaskList1.login).flashing("error" -> "User creation failed")
    }
    }.getOrElse(Redirect(routes.TaskList1.login))
  }

  def taskList = Action {implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map{ username =>
    val tasks = TaskListInMemoryModel.getTask(username)
    Ok(views.html.taskList1(tasks))
    }.getOrElse(Redirect(routes.TaskList1.login))
  }

  def logout = Action {
    Redirect(routes.TaskList1.login).withNewSession
  }


  def addTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map{ username => 
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val task = args("newTask").head // <- newTask at taskLis1.scala.html
        TaskListInMemoryModel.addTask(username, task);
        Redirect(routes.TaskList1.taskList)
      }.getOrElse(Redirect(routes.TaskList1.taskList))
    }.getOrElse(Redirect(routes.TaskList1.login))
  }

  def deleteTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map{ username => 
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val index = args("index").head.toInt
        TaskListInMemoryModel.removeTask(username, index);
        Redirect(routes.TaskList1.taskList)
      }.getOrElse(Redirect(routes.TaskList1.taskList))
    }.getOrElse(Redirect(routes.TaskList1.login))
  }




}
