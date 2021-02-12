package com.example.draft_laundry.LaundryMain;

import android.widget.Button;

public class Dryer {
    public Button bt;
    public Boolean isError;
    public Boolean isTimer;

    public Dryer(Button bt, Boolean isError, Boolean isTimer) {
        this.bt = bt;
        this.isError = isError;
        this.isTimer = isTimer;
    }
}
