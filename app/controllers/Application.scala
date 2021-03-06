package controllers

import play.api.libs.json.JsValue
import play.api.mvc.{ Action, Controller, WebSocket }

import models.{ WebInstance, ErrorPropagationProtocol }

object Application extends Controller with ErrorPropagationProtocol {

  def index = Action {
    implicit request =>
      Ok(views.html.index())
  }

  def client(usernameOpt: Option[String]) = Action {
    implicit request =>
      usernameOpt map (
        username => Ok(views.html.client(username))
      ) getOrElse (
        Redirect(routes.Application.index()).flashing(
          ErrorKey -> "Please choose a valid username."
        )
      )
  }

  def handleSocketConnection(username: String, room: Int = 0) = WebSocket.async[JsValue] {
    implicit request =>
      WebInstance.join(username, room)
  }

}
