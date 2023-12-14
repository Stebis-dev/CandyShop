package com.CandyShop.controllers.mainShop.tableViewGuidelines;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

import java.time.LocalDate;

public class DatePickerTableCell<S> extends TableCell<S, LocalDate> {

    private final DatePicker datePicker;

    public DatePickerTableCell() {
        this.datePicker = new DatePicker();
        datePicker.setEditable(true);

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (isEditing()) {
                commitEdit(newDate);
            }
        });
    }

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                datePicker.setValue(item);
                setGraphic(datePicker);
                setText(null);
            } else {
                setText(item != null ? item.toString() : null);
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        datePicker.setValue(getItem());
        setGraphic(datePicker);
        setText(null);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() != null ? getItem().toString() : null);
        setGraphic(null);
    }

    @Override
    public void commitEdit(LocalDate newValue) {
        super.commitEdit(newValue);
        setText(newValue != null ? newValue.toString() : null);
        setGraphic(null);
    }
}
