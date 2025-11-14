package com.example;

import javafx.scene.control.TextField;

public class FxInputField implements InputField {

    private final TextField textField;

    public FxInputField(TextField field) {
        this.textField = field;
    }

    @Override
    public String getText() {
        return textField.getText();
    }
    @Override
    public void clear() {
        textField.clear();
    }
    public TextField getFxField() {
        return textField;
    }
}
