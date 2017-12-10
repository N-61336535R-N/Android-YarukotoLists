package ngrnm.syokuninn_sibou.yarukotolists.Timer.Utils;

/**
 * タイマー部分で使う数値群のオブジェクト
 * セットしたいタイマー１セットにつき一つのインスタンスを作る。
 */

public class Timer {
    // タイマー時間。タイマーを実行する時は、この値を使う。
    private int time = 0;  // 秒表記
    private int time_h = 0;  // 時
    private int time_m = 0;  // 分
    private int time_s = 0;  // 秒

    public int getTime() {
        return time;
    }
    public void setTime(int h, int m, int s) {
        this.time_h = h;
        this.time_m = m;
        this.time_s = s;
        this.strTime_h_m_s = String.format("%1$d:%2$02d:%3$02ds", h, m, s);
        this.time = h*360 + m*60 + s;
        this.strTime = Integer.toString(time);
    }

    // タイマー時間のString表記。ゲッターには、「秒表記」と、「時:分:秒表記」がある。
    private String strTime="0s";
    private String strTime_h_m_s="0:00:00s";

    public String getStrTime() {
        return strTime;
    }
    public String getStrTime_h_m_s() {
        // 残り時間を分、秒、ミリ秒に分割
        return strTime_h_m_s;
    }
}
