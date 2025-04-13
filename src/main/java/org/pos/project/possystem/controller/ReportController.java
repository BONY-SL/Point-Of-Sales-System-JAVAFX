package org.pos.project.possystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.pos.project.possystem.util.CommonMethod;

public class ReportController {

    @FXML
    void goBack(ActionEvent event) {

        CommonMethod.goToBack(event,getClass());

    }
}
