package apexsx.forge.core;

import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("SpellCheckingInspection")
public class HexUtil {

    /**
     * hex字符串转byte[]，自动过滤空格、换行、tab、非hex字符
     * @param hexInput 原始hex字符串，可以混杂空格 \t \n \r
     * @return 二进制字节数组
     */
    public static byte[] hexToBytes(String hexInput) {
        // 只保留 0-9 a-f A-F，其余全部剔除
        StringBuilder sb = new StringBuilder();
        for (char ch : hexInput.toCharArray()) {
            if ((ch >= '0' && ch <= '9')
                    || (ch >= 'a' && ch <= 'f')
                    || (ch >= 'A' && ch <= 'F')) {
                sb.append(ch);
            }
        }
        String cleanHex = sb.toString();
        // 奇数个字符，舍弃末尾单个字符
        if (cleanHex.length() % 2 != 0) {
            cleanHex = cleanHex.substring(0, cleanHex.length() - 1);
        }

        int len = cleanHex.length();
        byte[] result = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            String pair = cleanHex.substring(i, i + 2);
            result[i / 2] = (byte) Integer.parseInt(pair, 16);
        }
        return result;
    }

    /**
     * hex字符串直接写出为二进制文件
     * @param hexInput hex原始字符串
     * @param outputPath 输出文件路径
     * @throws IOException IO异常
     */
    public static void hexToFile(String hexInput, String outputPath) throws IOException {
        byte[] data = hexToBytes(hexInput);
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(data);
        }
    }

    // Demo main
    public static void main(String[] args) throws IOException {
        String hexSample =
                "377ABCAF27 1C0004B01B CFD0E30100 0000000000 5A00000000 000000014A 35B3E00994 01DB5D0038 184899CD17 752408559A\n" +
                "168CDF8ACE D74372CC0B A918743984 4A76D372CC A08F0C4CB4 662DD5B7E8 3BC2BC2B14 8B1560486A 9A56B6CFEB 5344BC7ED0\n" +
                "A892F704AE 085CB8D0EA 61F6A22627 C50936C7C7 C38CCEA7F0 01D61B810E 34743E442E D0C20EC949 798078D4E3 6CDB1404D3\n" +
                "8B9E2808E6 04FC5FBCAA 89AE5A6870 58D3966858 EE348236B0 A63204405F 5BEDC95F3C 4FFE76EFBA 19314DC097 551F1FCA42\n" +
                "361C38DFC6 E497668AD8 8FD3BAE428 E6231BC110 435AF5C5C0 61CEA3EF2D 4BC6D3BDE7 D3F45E3267 A186CAF2BA 8AF9D10A6E\n" +
                "C29F05BD3B 1314B95C53 27CC3AAC92 0CD9FEF2F1 0660FBDA88 C561732477 8A1460ADE4 FFC5601C1F A4E673FB22 010C80962D\n" +
                "BF25973F26 6D281ED5CE E1135251A7 DE1843BD09 CD3D6697B0 3D7E3B4B4B DEAD51012F 41DD9884A8 77F7E713F6 271FADA4DA\n" +
                "574F03B35C 81E1EC3CE6 E2E22AD813 7B9C835A94 375FCCC0AE B64D64428E 1F45349BE5 52F5CE4AD2 6AD128B585 AD9DC63409\n" +
                "BD13A6DA66 8BDA3C5AAA 5D8FDE8A37 9821D858B3 9EAE2E6E11 5F704A13C0 69AB61FFBA 15E1200464 4F2BCED390 6543EFE7FD\n" +
                "4BFE47E93D 172EEDB003 EAD46DD9F9 A17D727726 FF2CA807FF 83F39DAD2E 61E2B80711 41598551EA CFEE26AE2F 4C80AE866E\n" +
                "7330DBD314 1D9D0F3E16 CE4A98A000 0104060001 0981E30007 0B01000121 2101000C89 9500080A01 5E25F2F300 000501190A\n" +
                "0000000000 0000000000 110F005700 2E006A0061 0076006100 0000190400 000000140A 0100AE26D3 9AAB3ADD01 1506010020\n" +
                "0000000000";

        // 转字节数组
        byte[] bytes = hexToBytes(hexSample);
        System.out.println("bytes length: " + bytes.length);

        // 输出到bin文件
        hexToFile(hexSample, "./out.7z");
    }
}