package com.codecool.krk.controller;

import com.codecool.krk.model.Timer;
import com.codecool.krk.model.TimerFactory;
import com.codecool.krk.service.TimerService;
import com.codecool.krk.view.TimerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TimerController {
    private TimerFactory timerFactory;
    private TimerView timerView;
    private TimerService timerService;

    public TimerController(TimerFactory timerFactory, TimerView timerView, TimerService timerService) {
        this.timerFactory = timerFactory;
        this.timerView = timerView;
        this.timerService = timerService;
    }

    public void chooseOption() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            this.timerView.setBufferedReader(bufferedReader);

            boolean exitApplication = false;

            do {
                String[] userInput = this.timerView.getUserInput();
                MenuOption userChoice = MenuOption.valueOf(userInput[0].toUpperCase());

                switch (userChoice) {
                    case START:
                        createTimer(userInput);
                        break;
                    case CHECK:
                        this.timerService.checkTimer(userInput);
                        break;
                    case STOP:
                        stopTimer(userInput);
                        break;
                    case EXIT:
                        exitApplication = this.timerService.interruptAllTimers();
                        break;
                    default:
                        this.timerView.displayMessage("Wrong command");
                        break;
                }
            } while (!exitApplication);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.timerView.displayTimers(Timer.getTimers());
    }

    private void createTimer(String[] userInput) {
        try {
            this.timerService.validateArgumentsNumber(userInput, "Timer name no specified");
            timerFactory.createTimer(userInput);
        } catch (IllegalArgumentException e) {
            this.timerView.displayMessage(e.getMessage());
        }
    }

    private void stopTimer(String[] userInput) {
        try {
            this.timerService.validateArgumentsNumber(userInput, "Timer name no specified");

            if (this.timerService.interruptTimer(userInput)) {
                String timerName = userInput[1];
                this.timerView.displayMessage(String.format("%s received interrupt while sleeping", timerName));
            } else {
                this.timerView.displayMessage("Operation unsuccessful due to wrong timer name or timer already stopped");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
