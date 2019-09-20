package interface

import java.io.FileInputStream

import scalafx.Includes.handle
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, TextInputDialog}
import scalafx.scene.image.Image
import scalafx.scene.layout._

object Menu extends JFXApp {
  private val newGameButton = new Button("New game")

  private val playerRegisterButton = new Button("Register new player")
  playerRegisterButton.onAction = handle {
    val dialog = new TextInputDialog() {
      initOwner(stage)
      title = "Register new player"
      headerText = "Welcome to the new player registration service!"
      contentText = "Please enter your name:"
    }

    val result = dialog.showAndWait()

    result match {
      case Some(name) =>
        Resource.registerPlayer(name) match {
          case None =>
            new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Error"
              headerText = "Error registering the new player."
              contentText = "Sorry, that name is already taken. Please choose another name."
            }.showAndWait()
          case Some(_) =>
        }
      case None =>
    }
  }

  private val exitButton = new Button("Exit")
  exitButton.setCancelButton(true)
  exitButton.onAction = handle {
    System.exit(0)
  }

  private val tableBackground = new Background(Array(new BackgroundImage(
    new Image(new FileInputStream(Resource.imageFolder + "/table.jpg")), BackgroundRepeat.NoRepeat,
    BackgroundRepeat.NoRepeat, new BackgroundPosition(BackgroundPosition.Default), BackgroundSize.Default)))

  private val menuScene = new Scene {
    content = new Pane {
      children = new HBox {
        padding = Insets(20)
        children = Array(newGameButton, playerRegisterButton, exitButton)
      }
      background = tableBackground
    }
  }

  stage = new PrimaryStage {
    title = "The Elder Scrolls Legends Helper"
    scene = menuScene
  }

  def startMenu(): Unit = {
    main(Array())
  }
}
