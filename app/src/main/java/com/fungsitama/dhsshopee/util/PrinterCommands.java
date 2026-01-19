package com.fungsitama.dhsshopee.util;

import static com.fungsitama.dhsshopee.activity.printer.PrinterCommands.FS;
import static com.fungsitama.dhsshopee.activity.printer.PrinterCommands.GS;

public class PrinterCommands {
    public static final byte CAN = 24;
    public static final byte CLR = 12;

    /* renamed from: CR */
    public static final byte f170CR = 13;
    public static final byte DLE = 16;
    public static final byte EOT = 4;
    public static final byte ESC = 27;
    public static final byte[] ESC_ALIGN_CENTER = {ESC, 97, 1};
    public static final byte[] ESC_ALIGN_LEFT = {ESC, 97, 0};
    public static final byte[] ESC_ALIGN_RIGHT = {ESC, 97, 2};
    public static final byte[] ESC_CANCEL_BOLD = {ESC, 69, 0};
    public static final byte[] ESC_CANCLE_HORIZONTAL_CENTERS = {ESC, 68, 0};
    public static final byte[] ESC_ENTER = {ESC, 74, 64};
    public static final byte[] ESC_FONT_COLOR_DEFAULT = {ESC, 114, 0};
    public static final byte[] ESC_HORIZONTAL_CENTERS = {ESC, 68, 20, FS, 0};
    public static final byte[] FEED_LINE = {10};
    public static byte[] FEED_PAPER_AND_CUT = {GS, 86, 66, 0};

    /* renamed from: FS */
    public static final byte f171FS = 28;
    public static final byte[] FS_FONT_ALIGN = {f171FS, 33, 1, ESC, 33, 1};

    /* renamed from: GS */
    public static final byte f172GS = 29;

    /* renamed from: HT */
    public static final byte f173HT = 9;
    public static final byte[] INIT = {ESC, 64};

    /* renamed from: LF */
    public static final byte f174LF = 10;
    public static final byte[] PRINTE_TEST = {f172GS, 40, 65};
    public static byte[] PRINT_BAR_CODE_1 = {f172GS, 107, 2};
    public static final byte[] SELECT_BIT_IMAGE_MODE = {ESC, 42, 33, -1, 3};
    public static byte[] SELECT_CYRILLIC_CHARACTER_CODE_TABLE = {ESC, 116, 17};
    public static byte[] SELECT_FONT_A = {20, 33, 0};
    public static byte[] SELECT_PRINT_SHEET = {ESC, 99, 48, 2};
    public static byte[] SEND_NULL_BYTE = {0};
    public static byte[] SET_BAR_CODE_HEIGHT = {f172GS, 104, 100};
    public static final byte[] SET_LINE_SPACING_24 = {ESC, 51, CAN};
    public static final byte[] SET_LINE_SPACING_30 = {ESC, 51, 30};
    public static final byte STX = 2;
    public static byte[] TRANSMIT_DLE_ERROR_STATUS = {DLE, 4, 3};
    public static byte[] TRANSMIT_DLE_OFFLINE_PRINTER_STATUS = {DLE, 4, 2};
    public static byte[] TRANSMIT_DLE_PRINTER_STATUS = {DLE, 4, 1};
    public static byte[] TRANSMIT_DLE_ROLL_PAPER_SENSOR_STATUS = {DLE, 4, 4};

    /* renamed from: US */
    public static final byte f175US = 31;
}
