package com.webstocker.utilitaires;

import org.springframework.stereotype.Component;

@Component
public class OperationString {

    public String capitalize(String inputString) {

        char firstLetter = inputString.charAt(0);
        char capitalFirstLetter = Character.toUpperCase(firstLetter);

        return inputString.replace(inputString.charAt(0), capitalFirstLetter);
    }
}
