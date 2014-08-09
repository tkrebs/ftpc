/*
 * FTP Crawler - A simple file crawler for UNIX based FTP servers
 * Copyright (C) 2014 Tobias Krebs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.ep3.ftpc.controller;

import de.ep3.ftpc.App;
import de.ep3.ftpc.model.StatusManager;
import de.ep3.ftpc.model.i18n.I18nManager;

import javax.swing.*;

/**
 * Displays all uncaught exceptions as error messages and then exits the application.
 */
public class ExceptionController implements Controller, Thread.UncaughtExceptionHandler
{

    @Override
    public void dispatch()
    {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void setI18nManager(I18nManager i18n)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatusManager(StatusManager status)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e)
    {
        String title = "Whoooa - what did just happen?";
        String message = "Something went wrong here and the application had no other choice than to stop.";

        String errorMessage = e.getMessage();

        if (errorMessage == null || errorMessage.length() == 0) {
            errorMessage = e.getClass().toString() + " has been thrown";
        }

        String previewErrorMessage = errorMessage;

        if (previewErrorMessage.length() > 128) {
            previewErrorMessage = previewErrorMessage.substring(0, 128) + " ... (and more)";
        }

        if (previewErrorMessage.length() > 8) {
            message += " This is why:\n\n" + previewErrorMessage;
        }

        String[] dialogOptions = new String[]{ "OK", "Tell me more" };

        int dialogChoice = JOptionPane.showOptionDialog(null, message, title,
            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, dialogOptions, null);

        switch (dialogChoice) {
            case 1:
                boolean displayStackTrace = true;

                if (! errorMessage.equals(previewErrorMessage)) {
                    String fullMessage = errorMessage.replace(":", ":\n");

                    dialogOptions = new String[]{ "OK", "Tell me even more" };

                    dialogChoice = JOptionPane.showOptionDialog(null, fullMessage, title,
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, dialogOptions, null);

                    if (dialogChoice == 0) {
                        displayStackTrace = false;
                    }
                }

                if (displayStackTrace) {
                    String stackTraceMessage = "Stack trace:";

                    for (StackTraceElement ste : e.getStackTrace()) {
                        stackTraceMessage += "\n" + ste.toString();
                    }

                    JOptionPane.showMessageDialog(null, stackTraceMessage, title, JOptionPane.ERROR_MESSAGE);
                }

                break;
        }

        App.exit(1);
    }

}