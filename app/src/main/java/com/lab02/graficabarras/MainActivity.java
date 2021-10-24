package com.lab02.graficabarras;

import androidx.appcompat.app.AppCompatActivity;
import com.lab02.graficabarras.BarChartView;
import com.lab02.graficabarras.BarData;
import java.util.ArrayList;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BarChartView barChartView = (BarChartView) findViewById(R.id.barChart);
        ArrayList arrayList = new ArrayList<>();
        BarData barData = new BarData("C", 100);
        arrayList.add(barData);
        barData = new BarData("Java", 50);
        arrayList.add(barData);
        barData = new BarData("Python", 150);
        arrayList.add(barData);
        barData = new BarData("C++", 60);
        arrayList.add(barData);
        barData = new BarData("C#", 100);
        arrayList.add(barData);
        barData = new BarData("JS", 20);
        arrayList.add(barData);
        barData = new BarData("Kotlin", 40);
        arrayList.add(barData);


        BarData[] arrayBarData = (BarData[]) arrayList.toArray((new BarData[arrayList.size()]));
        barChartView.dibujarGraficoData(arrayBarData);
    }
}
