package com.lab02.graficabarras;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import com.lab02.graficabarras.BarData;

public class BarChartView extends View {
    private Paint mPaint;
    private Context mContext;
    private BarData[] data;
    private float maximoValorData;
    private final int ancho = 2;
    private int tamanoLetra = 14;
    private int maximoValorY = 9;
    private int ditanciaLayoutValor;
    private int anchoEjeY;
    private int anchoEjeX;

    /**
     * Constuctor
     */
    public BarChartView(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
        mContext = context;
        mPaint = new Paint();
        init();
    }

    /**
     * Inicializar variables internas
     */
    private void init() {

        ditanciaLayoutValor = (int) dpToPixels(mContext, 14);
    }

    /**
     * Dijando el grafico con los datos proporcionados por BarData
     */
    public void dibujarGraficoData(BarData[] barData) {

        data = barData;
        maximoValorData = Float.MIN_VALUE;
        for (int index = 0; index < data.length; index++) {
            if (maximoValorData < data[index].getValor())
                maximoValorData = data[index].getValor();
        }
        anchoMaximoTexto(barData);
        invalidate();
    }

    /**
     * Devuelve el valor máximo en el conjunto de datos.
     */
    public float getValorMaximoData() {

        return maximoValorData;
    }

    /**
     * Devuelve el ancho máximo ocupado por cualquiera de los valores del eje Y.
     */
    private int getMaximoEjeY() {

        return anchoEjeY;
    }

    /**
     * Calculamos el ancho máximo ocupado por cualquiera de los datos del gráfico de barras.
     */
    private void anchoMaximoTexto(BarData[] barDatas) {

        anchoEjeY = Integer.MIN_VALUE;
        anchoEjeX = Integer.MIN_VALUE;

        Paint paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(dpToPixels(mContext, tamanoLetra));

        Rect bounds = new Rect();

        for (int index = 0; index < data.length; index++) {
            int currentTextWidth =
                    (int) paint.measureText(Float.toString(barDatas[index].getValor()));
            if (anchoEjeY < currentTextWidth)
                anchoEjeY = currentTextWidth;

            mPaint.getTextBounds(barDatas[index].getLayoutX(), 0,
                    barDatas[index].getLayoutX().length(), bounds);
            if (anchoEjeX < bounds.height())
                anchoEjeX = bounds.height();
        }
    }

    /**
     * Devuelvo la altura maxima en el eje X
     */
    public int getMaximaAlturaTextoEjeX() {

        return anchoEjeX;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int usableViewHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        int usableViewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        Point origin = getOrigen();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
        mPaint.setStrokeWidth(ancho);
        //dibujar y eje
        canvas.drawLine(origin.x, origin.y, origin.x,
                origin.y - (usableViewHeight - maximaAlturaEjeXMargen()), mPaint);
        //dibujar y eje
        mPaint.setStrokeWidth(ancho + 1);
        canvas.drawLine(origin.x, origin.y,
                origin.x + usableViewWidth -
                        (getMaximoEjeY() +
                                ditanciaLayoutValor), origin.y, mPaint);

        if (data == null || data.length == 0)
            return;
        //dibujar barras
        int barAndVacantSpaceCount = (data.length << 1) + 1;
        int widthFactor = (usableViewWidth - getMaximoEjeY()) / barAndVacantSpaceCount;
        int x1, x2, y1, y2;
        float maxValue = getValorMaximoData();
        for (int index = 0; index < data.length; index++) {
            x1 = origin.x + ((index << 1) + 1) * widthFactor;
            x2 = origin.x + ((index << 1) + 2) * widthFactor;
            int barHeight = (int) ((usableViewHeight - maximaAlturaEjeXMargen()) *
                    data[index].getValor() / maxValue);
            y1 = origin.y - barHeight;
            y2 = origin.y;
            canvas.drawRect(x1, y1, x2, y2, mPaint);
            dibujarEjeX(origin, data[index].getLayoutX(), x1 + (x2 - x1) / 2, canvas);
        }
        dibujarEjeY(origin, (usableViewHeight - maximaAlturaEjeXMargen()), canvas);
    }

    /**
     * Formatea el valor flotante dado hasta un punto de precisión decimal.
     */
    private String getPrecisionDecimal(float value) {

        DecimalFormat precision = new DecimalFormat("0.0");
        return precision.format(value);
    }

    /**
     * Dibuja etiquetas del eje Y y puntos de marcador a lo largo del eje Y.
     */
    public void dibujarEjeY(Point origin, int usableViewHeight, Canvas canvas) {

        float maxValueOfData = (int) getValorMaximoData();
        float yAxisValueInterval = usableViewHeight / maximoValorY;
        float dataInterval = maxValueOfData / maximoValorY;
        float valueToBeShown = maxValueOfData;
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(dpToPixels(mContext, tamanoLetra));

        //dibujar todos los textos de arriba a abajo
        for (int index = 0; index < maximoValorY; index++) {
            String string = getPrecisionDecimal(valueToBeShown);

            Rect bounds = new Rect();
            mPaint.getTextBounds(string, 0, string.length(), bounds);
            int y = (int) ((origin.y - usableViewHeight) + yAxisValueInterval * index);
            canvas.drawLine(origin.x - (ditanciaLayoutValor >> 1), y, origin.x, y, mPaint);
            y = y + (bounds.height() >> 1);
            canvas.drawText(string, origin.x - bounds.width() - ditanciaLayoutValor, y, mPaint);
            valueToBeShown = valueToBeShown - dataInterval;
        }
    }

    /**
     * Dibuja etiquetas del eje X.
     */
    public void dibujarEjeX(Point origin, String label, int centerX, Canvas canvas) {

        Rect bounds = new Rect();
        mPaint.getTextBounds(label, 0, label.length(), bounds);
        int y = origin.y + ditanciaLayoutValor + getMaximaAlturaTextoEjeX();
        int x = centerX - bounds.width() / 2;
        mPaint.setTextSize(dpToPixels(mContext, tamanoLetra));
        mPaint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(label, x, y, mPaint);
    }

    /**
     * Devuelve la maxima altura del eje X y margen de la etiqueta
     */
    private int maximaAlturaEjeXMargen() {

        return getMaximaAlturaTextoEjeX() + ditanciaLayoutValor;
    }

    /**
     * Devuelve el origen para las coordendanas del lienzo
     */
    public Point getOrigen() {

        if (data != null) {

            return new Point(getPaddingLeft() + getMaximoEjeY() + ditanciaLayoutValor,
                    getHeight() - getPaddingBottom() - maximaAlturaEjeXMargen());
        } else {

            return new Point(getPaddingLeft() + getMaximoEjeY() + ditanciaLayoutValor,
                    getHeight() - getPaddingBottom());
        }
    }

    /**
     * Convierta el valor dp a píxeles.
     */
    public static float dpToPixels(Context context, float dpValue) {

        if (context != null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, metrics);
        }
        return 0;
    }

}
