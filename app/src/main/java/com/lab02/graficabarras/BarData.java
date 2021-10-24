package com.lab02.graficabarras;

public class BarData {
    private String layoutX;
    private float valor;

    public BarData(String l, float v) {

        layoutX = l;
        valor = v;
    }

    public String getLayoutX() {

        return layoutX;
    }

    public void setXAxisName(String l) {

        layoutX = l;
    }

    public float getValor() {

        return valor;
    }

    public void setValue(float v) {

        valor = v;
    }
}
