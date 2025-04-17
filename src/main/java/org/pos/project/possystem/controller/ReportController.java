package org.pos.project.possystem.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.pos.project.possystem.dto.ProductQtyReportDTO;
import org.pos.project.possystem.model.ReportModel;
import org.pos.project.possystem.util.CommonMethod;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportController {

    @FXML
    private DatePicker datePicker;


    @FXML
    void productQtyByDate(ActionEvent event) {

        try{
            InputStream getReport = getClass().getResourceAsStream("/org/pos/project/possystem/reports/ProductQty.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(getReport);
            List<ProductQtyReportDTO> dataBeanList = new ArrayList<>();

            String date = String.valueOf(datePicker.getValue());

            Date sqlDate = Date.valueOf(date);

            ObservableList<ProductQtyReportDTO> productModels = ReportModel.getProQtyByDate(sqlDate);

            productModels.forEach(productQtyReportDTO -> {
                dataBeanList.add(new ProductQtyReportDTO(productQtyReportDTO.getDate(), productQtyReportDTO.getId(), productQtyReportDTO.getQty()));
                System.out.println(productQtyReportDTO.getQty());
            });

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataBeanList);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportOneParameter", dataSource);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void goBack(ActionEvent event) {

        CommonMethod.goToBack(event,getClass());

    }
}
