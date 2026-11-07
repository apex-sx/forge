package pxf.toolkit.basic.lang;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Random;
import javax.annotation.Nonnull;
import pxf.toolkit.basic.concurrent.locks.ReentrantSpinLock;
import pxf.toolkit.basic.util.Extract;
import pxf.toolkit.basic.util.Is;

/**
 * 颜色工具类
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public final class ColorHelper {

  public static final float MAX_BYTE_VALUE = 255f;
  /** 锁 */
  private static final ReentrantSpinLock SECURE_RANDOM_LOCK = new ReentrantSpinLock();
  /*
   *浅粉红
   *255,182,193
   */
  public static int LIGHTPINK = 0XFFB6C1;
  /*
   *粉红
   *255,192,203
   */
  public static int PINK = 0XFFC0CB;
  /*
   *猩红
   *220,20,60
   */
  public static int CRIMSON = 0XDC143C;
  /*
   *脸红的淡紫色
   *255,240,245
   */
  public static int LAVENDERBLUSH = 0XFFF0F5;
  /*
   *苍白的紫罗兰红色
   *219,112,147
   */
  public static int PALEVIOLETRED = 0XDB7093;
  /*
   *热情的粉红
   *255,105,180
   */
  public static int HOTPINK = 0XFF69B4;
  /*
   *深粉色
   *255,20,147
   */
  public static int DEEPPINK = 0XFF1493;
  /*
   *适中的紫罗兰红色
   *199,21,133
   */
  public static int MEDIUMVIOLETRED = 0XC71585;
  /*
   *兰花的紫色
   *218,112,214
   */
  public static int ORCHID = 0XDA70D6;
  /*
   *蓟
   *216,191,216
   */
  public static int THISTLE = 0XD8BFD8;
  /*
   *李子
   *221,160,221
   */
  public static int PLUM = 0XDDA0DD;
  /*
   *紫罗兰
   *238,130,238
   */
  public static int VIOLET = 0XEE82EE;
  /*
   *洋红
   *255,0,255
   */
  public static int MAGENTA = 0XFF00FF;
  /*
   *灯笼海棠(紫红色)
   *255,0,255
   */
  public static int FUCHSIA = 0XFF00FF;
  /*
   *深洋红色
   *139,0,139
   */
  public static int DARKMAGENTA = 0X8B008B;
  /*
   *紫色
   *128,0,128
   */
  public static int PURPLE = 0X800080;
  /*
   *适中的兰花紫
   *186,85,211
   */
  public static int MEDIUMORCHID = 0XBA55D3;
  /*
   *深紫罗兰色
   *148,0,211
   */
  public static int DARKVOILET = 0X9400D3;
  /*
   *深兰花紫
   *153,50,204
   */
  public static int DARKORCHID = 0X9932CC;
  /*
   *靛青
   *75,0,130
   */
  public static int INDIGO = 0X4B0082;
  /*
   *深紫罗兰的蓝色
   *138,43,226
   */
  public static int BLUEVIOLET = 0X8A2BE2;
  /*
   *适中的紫色
   *147,112,219
   */
  public static int MEDIUMPURPLE = 0X9370DB;
  /*
   *适中的板岩暗蓝灰色
   *123,104,238
   */
  public static int MEDIUMSLATEBLUE = 0X7B68EE;
  /*
   *板岩暗蓝灰色
   *106,90,205
   */
  public static int SLATEBLUE = 0X6A5ACD;
  /*
   *深岩暗蓝灰色
   *72,61,139
   */
  public static int DARKSLATEBLUE = 0X483D8B;
  /*
   *熏衣草花的淡紫色
   *230,230,250
   */
  public static int LAVENDER = 0XE6E6FA;
  /*
   *幽灵的白色
   *248,248,255
   */
  public static int GHOSTWHITE = 0XF8F8FF;
  /*
   *纯蓝
   *0,0,255
   */
  public static int BLUE = 0X0000FF;
  /*
   *适中的蓝色
   *0,0,205
   */
  public static int MEDIUMBLUE = 0X0000CD;
  /*
   *午夜的蓝色
   *25,25,112
   */
  public static int MIDNIGHTBLUE = 0X191970;
  /*
   *深蓝色
   *0,0,139
   */
  public static int DARKBLUE = 0X00008B;
  /*
   *海军蓝
   *0,0,128
   */
  public static int NAVY = 0X000080;
  /*
   *皇家蓝
   *65,105,225
   */
  public static int ROYALBLUE = 0X4169E1;
  /*
   *矢车菊的蓝色
   *100,149,237
   */
  public static int CORNFLOWERBLUE = 0X6495ED;
  /*
   *淡钢蓝
   *176,196,222
   */
  public static int LIGHTSTEELBLUE = 0XB0C4DE;
  /*
   *浅石板灰
   *119,136,153
   */
  public static int LIGHTSLATEGRAY = 0X778899;
  /*
   *石板灰
   *112,128,144
   */
  public static int SLATEGRAY = 0X708090;
  /*
   *道奇蓝
   *30,144,255
   */
  public static int DODERBLUE = 0X1E90FF;
  /*
   *爱丽丝蓝
   *240,248,255
   */
  public static int ALICEBLUE = 0XF0F8FF;
  /*
   *钢蓝
   *70,130,180
   */
  public static int STEELBLUE = 0X4682B4;
  /*
   *淡蓝色
   *135,206,250
   */
  public static int LIGHTSKYBLUE = 0X87CEFA;
  /*
   *天蓝色
   *135,206,235
   */
  public static int SKYBLUE = 0X87CEEB;
  /*
   *深天蓝
   *0,191,255
   */
  public static int DEEPSKYBLUE = 0X00BFFF;
  /*
   *淡蓝
   *173,216,230
   */
  public static int LIGHTBLUE = 0XADD8E6;
  /*
   *火药蓝
   *176,224,230
   */
  public static int POWDERBLUE = 0XB0E0E6;
  /*
   *军校蓝
   *95,158,160
   */
  public static int CADETBLUE = 0X5F9EA0;
  /*
   *蔚蓝色
   *240,255,255
   */
  public static int AZURE = 0XF0FFFF;
  /*
   *淡青色
   *225,255,255
   */
  public static int LIGHTCYAN = 0XE1FFFF;
  /*
   *苍白的绿宝石
   *175,238,238
   */
  public static int PALETURQUOISE = 0XAFEEEE;
  /*
   *青色
   *0,255,255
   */
  public static int CYAN = 0X00FFFF;
  /*
   *水绿色
   *212,242,231
   */
  public static int AQUA = 0XD4F2E7;
  /*
   *深绿宝石
   *0,206,209
   */
  public static int DARKTURQUOISE = 0X00CED1;
  /*
   *深石板灰
   *47,79,79
   */
  public static int DARKSLATEGRAY = 0X2F4F4F;
  /*
   *深青色
   *0,139,139
   */
  public static int DARKCYAN = 0X008B8B;
  /*
   *水鸭色
   *0,128,128
   */
  public static int TEAL = 0X008080;
  /*
   *适中的绿宝石
   *72,209,204
   */
  public static int MEDIUMTURQUOISE = 0X48D1CC;
  /*
   *浅海洋绿
   *32,178,170
   */
  public static int LIGHTSEAGREEN = 0X20B2AA;
  /*
   *绿宝石
   *64,224,208
   */
  public static int TURQUOISE = 0X40E0D0;
  /*
   *绿玉\碧绿色
   *127,255,170
   */
  public static int AUQAMARIN = 0X7FFFAA;
  /*
   *适中的碧绿色
   *0,250,154
   */
  public static int MEDIUMAQUAMARINE = 0X00FA9A;
  /*
   *适中的春天的绿色
   *0,255,127
   */
  public static int MEDIUMSPRINGGREEN = 0X00FF7F;
  /*
   *薄荷奶油
   *245,255,250
   */
  public static int MINTCREAM = 0XF5FFFA;
  /*
   *春天的绿色
   *60,179,113
   */
  public static int SPRINGGREEN = 0X3CB371;
  /*
   *海洋绿
   *46,139,87
   */
  public static int SEAGREEN = 0X2E8B57;
  /*
   *蜂蜜
   *240,255,240
   */
  public static int HONEYDEW = 0XF0FFF0;
  /*
   *淡绿色
   *144,238,144
   */
  public static int LIGHTGREEN = 0X90EE90;
  /*
   *苍白的绿色
   *152,251,152
   */
  public static int PALEGREEN = 0X98FB98;
  /*
   *深海洋绿
   *143,188,143
   */
  public static int DARKSEAGREEN = 0X8FBC8F;
  /*
   *酸橙绿
   *50,205,50
   */
  public static int LIMEGREEN = 0X32CD32;
  /*
   *酸橙色
   *0,255,0
   */
  public static int LIME = 0X00FF00;
  /*
   *森林绿
   *34,139,34
   */
  public static int FORESTGREEN = 0X228B22;
  /*
   *纯绿
   *0,128,0
   */
  public static int GREEN = 0X008000;
  /*
   *深绿色
   *0,100,0
   */
  public static int DARKGREEN = 0X006400;
  /*
   *查特酒绿
   *127,255,0
   */
  public static int CHARTREUSE = 0X7FFF00;
  /*
   *草坪绿
   *124,252,0
   */
  public static int LAWNGREEN = 0X7CFC00;
  /*
   *绿黄色
   *173,255,47
   */
  public static int GREENYELLOW = 0XADFF2F;
  /*
   *橄榄土褐色
   *85,107,47
   */
  public static int OLIVEDRAB = 0X556B2F;
  /*
   *米色(浅褐色)
   *245,245,220
   */
  public static int BEIGE = 0XF5F5DC;
  /*
   *浅秋麒麟黄
   *250,250,210
   */
  public static int LIGHTGOLDENRODYELLOW = 0XFAFAD2;
  /*
   *象牙
   *255,255,240
   */
  public static int IVORY = 0XFFFFF0;
  /*
   *浅黄色
   *255,255,224
   */
  public static int LIGHTYELLOW = 0XFFFFE0;
  /*
   *纯黄
   *255,255,0
   */
  public static int YELLOW = 0XFFFF00;
  /*
   *橄榄
   *128,128,0
   */
  public static int OLIVE = 0X808000;
  /*
   *深卡其布
   *189,183,107
   */
  public static int DARKKHAKI = 0XBDB76B;
  /*
   *柠檬薄纱
   *255,250,205
   */
  public static int LEMONCHIFFON = 0XFFFACD;
  /*
   *灰秋麒麟
   *238,232,170
   */
  public static int PALEGODENROD = 0XEEE8AA;
  /*
   *卡其布
   *240,230,140
   */
  public static int KHAKI = 0XF0E68C;
  /*
   *金
   *255,215,0
   */
  public static int GOLD = 0XFFD700;
  /*
   *玉米色
   *255,248,220
   */
  public static int CORNISLK = 0XFFF8DC;
  /*
   *秋麒麟
   *218,165,32
   */
  public static int GOLDENROD = 0XDAA520;
  /*
   *花的白色
   *255,250,240
   */
  public static int FLORALWHITE = 0XFFFAF0;
  /*
   *老饰带
   *253,245,230
   */
  public static int OLDLACE = 0XFDF5E6;
  /*
   *小麦色
   *245,222,179
   */
  public static int WHEAT = 0XF5DEB3;
  /*
   *鹿皮鞋
   *255,228,181
   */
  public static int MOCCASIN = 0XFFE4B5;
  /*
   *橙色
   *255,165,0
   */
  public static int ORANGE = 0XFFA500;
  /*
   *番木瓜
   *255,239,213
   */
  public static int PAPAYAWHIP = 0XFFEFD5;
  /*
   *漂白的杏仁
   *255,235,205
   */
  public static int BLANCHEDALMOND = 0XFFEBCD;
  /*
   *纳瓦霍白
   *255,222,173
   */
  public static int NAVAJOWHITE = 0XFFDEAD;
  /*
   *古代的白色
   *250,235,215
   */
  public static int ANTIQUEWHITE = 0XFAEBD7;
  /*
   *晒黑
   *210,180,140
   */
  public static int TAN = 0XD2B48C;
  /*
   *结实的树
   *222,184,135
   */
  public static int BRULYWOOD = 0XDEB887;
  /*
   *(浓汤)乳脂,番茄等
   *255,228,196
   */
  public static int BISQUE = 0XFFE4C4;
  /*
   *深橙色
   *255,140,0
   */
  public static int DARKORANGE = 0XFF8C00;
  /*
   *亚麻布
   *250,240,230
   */
  public static int LINEN = 0XFAF0E6;
  /*
   *秘鲁
   *205,133,63
   */
  public static int PERU = 0XCD853F;
  /*
   *桃色
   *255,218,185
   */
  public static int PEACHPUFF = 0XFFDAB9;
  /*
   *沙棕色
   *244,164,96
   */
  public static int SANDYBROWN = 0XF4A460;
  /*
   *巧克力
   *210,105,30
   */
  public static int CHOCOLATE = 0XD2691E;
  /*
   *马鞍棕色
   *139,69,19
   */
  public static int SADDLEBROWN = 0X8B4513;
  /*
   *海贝壳
   *255,245,238
   */
  public static int SEASHELL = 0XFFF5EE;
  /*
   *黄土赭色
   *160,82,45
   */
  public static int SIENNA = 0XA0522D;
  /*
   *浅鲜肉(鲑鱼)色
   *255,160,122
   */
  public static int LIGHTSALMON = 0XFFA07A;
  /*
   *珊瑚
   *255,127,80
   */
  public static int CORAL = 0XFF7F50;
  /*
   *橙红色
   *255,69,0
   */
  public static int ORANGERED = 0XFF4500;
  /*
   *深鲜肉(鲑鱼)色
   *233,150,122
   */
  public static int DARKSALMON = 0XE9967A;
  /*
   *番茄
   *255,99,71
   */
  public static int TOMATO = 0XFF6347;
  /*
   *薄雾玫瑰
   *255,228,225
   */
  public static int MISTYROSE = 0XFFE4E1;
  /*
   *鲜肉(鲑鱼)色
   *250,128,114
   */
  public static int SALMON = 0XFA8072;
  /*
   *雪
   *255,250,250
   */
  public static int SNOW = 0XFFFAFA;
  /*
   *淡珊瑚色
   *240,128,128
   */
  public static int LIGHTCORAL = 0XF08080;
  /*
   *玫瑰棕色
   *188,143,143
   */
  public static int ROSYBROWN = 0XBC8F8F;
  /*
   *印度红
   *205,92,92
   */
  public static int INDIANRED = 0XCD5C5C;
  /*
   *纯红
   *255,0,0
   */
  public static int RED = 0XFF0000;
  /*
   *棕色
   *165,42,42
   */
  public static int BROWN = 0XA52A2A;
  /*
   *耐火砖
   *178,34,34
   */
  public static int FIREBRICK = 0XB22222;
  /*
   *深红色
   *139,0,0
   */
  public static int DARKRED = 0X8B0000;
  /*
   *栗色
   *128,0,0
   */
  public static int MAROON = 0X800000;
  /*
   *纯白
   *255,255,255
   */
  public static int WHITE = 0XFFFFFF;
  /*
   *白烟
   *245,245,245
   */
  public static int WHITESMOKE = 0XF5F5F5;
  /*
   *亮灰色
   *220,220,220
   */
  public static int GAINSBORO = 0XDCDCDC;
  /*
   *浅灰色
   *211,211,211
   */
  public static int LIGHTGREY = 0XD3D3D3;
  /*
   *银白色
   *192,192,192
   */
  public static int SILVER = 0XC0C0C0;
  /*
   *深灰色
   *169,169,169
   */
  public static int DARKGRAY = 0XA9A9A9;
  /*
   *灰色
   *128,128,128
   */
  public static int GRAY = 0X808080;
  /*
   *暗淡的灰色
   *105,105,105
   */
  public static int DIMGRAY = 0X696969;
  /*
   *纯黑
   *0,0,0
   */
  public static int BLACK = 0X000000;
  /** 随机器 */
  private static volatile Random secureRandom = new Random();

  private ColorHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static void setSecureRandom(Random secureRandom) {
    SECURE_RANDOM_LOCK.lock();
    try {
      ColorHelper.secureRandom = secureRandom;
    } finally {
      SECURE_RANDOM_LOCK.unlock();
    }
  }

  @Nonnull
  public static float[] floatARGB(final int argb) {
    int[] r = ColorHelper.intARGB(argb);
    return new float[] {
      r[0] / MAX_BYTE_VALUE, r[1] / MAX_BYTE_VALUE, r[2] / MAX_BYTE_VALUE, r[3] / MAX_BYTE_VALUE
    };
  }

  @Nonnull
  public static int[] intARGB(final int argb) {
    int[] r = ColorHelper.intRGB(argb);
    int a = Extract.bitValFor8(argb, 4);
    return new int[] {r[0], r[1], r[2], a};
  }

  public static int[] randomARGB() {
    SECURE_RANDOM_LOCK.lock();
    try {
      return new int[] {
        secureRandom.nextInt(255),
        secureRandom.nextInt(255),
        secureRandom.nextInt(255),
        secureRandom.nextInt(255)
      };
    } finally {
      SECURE_RANDOM_LOCK.unlock();
    }
  }

  @Nonnull
  public static float[] floatRGB(final int rgb) {
    int[] r = ColorHelper.intRGB(rgb);
    return new float[] {r[0] / MAX_BYTE_VALUE, r[1] / MAX_BYTE_VALUE, r[2] / MAX_BYTE_VALUE};
  }

  @Nonnull
  public static int[] intRGB(final int rgb) {
    int b = Extract.bitValFor8(rgb, 1);
    int g = Extract.bitValFor8(rgb, 2);
    int r = Extract.bitValFor8(rgb, 3);
    return new int[] {r, g, b};
  }

  public static Color randomColor() {
    int[] rgb = randomRGB();
    return new Color(rgb[0], rgb[1], rgb[2]);
  }

  public static int[] randomRGB() {
    SECURE_RANDOM_LOCK.lock();
    try {
      return new int[] {
        secureRandom.nextInt(255), secureRandom.nextInt(255), secureRandom.nextInt(255)
      };
    } finally {
      SECURE_RANDOM_LOCK.unlock();
    }
  }

  /** */
  public static Color createColor(String input, Color defaultColor) {
    Color color;
    if (Is.empty(input)) {
      color = defaultColor;
    } else if (input.indexOf(",") > 0) {
      color = createColorFromSeparatedValues(input, ',', defaultColor);
    } else if (input.indexOf("#") > 0) {
      color = createColorFromSeparatedValues(input, '#', defaultColor);
    } else {
      color = createColorFromFieldValue(input, defaultColor);
    }
    return color;
  }

  /** */
  public static Color createColorFromSeparatedValues(
      String input, char separated, Color defaultColor) {
    return createColorFromValues(input.split(String.valueOf(separated)), defaultColor);
  }

  /** */
  public static Color createColorFromValues(String[] input, Color defaultColor) {
    if (Is.empty(input)) {
      return defaultColor;
    }
    try {
      if (input.length == 3) {
        int r = Integer.parseInt(input[0]);
        int g = Integer.parseInt(input[1]);
        int b = Integer.parseInt(input[2]);
        return new Color(r, g, b);
      } else if (input.length == 4) {
        int r = Integer.parseInt(input[0]);
        int g = Integer.parseInt(input[1]);
        int b = Integer.parseInt(input[2]);
        int a = Integer.parseInt(input[3]);
        return new Color(r, g, b, a);
      }
    } catch (NumberFormatException ignored) {
    }
    return defaultColor;
  }

  /** */
  public static Color createColorFromValues(int[] input, Color defaultColor) {
    if (Is.empty(input)) {
      return defaultColor;
    }
    if (input.length == 3) {
      return new Color(input[0], input[1], input[2]);
    } else if (input.length == 4) {
      return new Color(input[0], input[1], input[2], input[3]);
    }
    return defaultColor;
  }

  /** */
  public static Color createColorFromFieldValue(String input, Color defaultColor) {
    try {
      Field field = Class.forName("java.awt.Color").getField(input);
      return (Color) field.get(null);
    } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException ignored) {
    }
    return defaultColor;
  }
}
