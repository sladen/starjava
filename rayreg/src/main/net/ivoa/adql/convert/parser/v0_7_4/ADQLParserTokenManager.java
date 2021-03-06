/* Generated By:JavaCC: Do not edit this line. ADQLParserTokenManager.java */
package net.ivoa.adql.convert.parser.v0_7_4;
import net.ivoa.adql.convert.ADQLs2DOMParser;
import java.io.* ;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import javax.xml.transform.TransformerException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

public class ADQLParserTokenManager implements ADQLParserConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1)
{
   switch (pos)
   {
      case 0:
         if ((active1 & 0x400000L) != 0L)
            return 0;
         if ((active0 & 0x7ffffffffffffe0L) != 0L)
         {
            jjmatchedKind = 66;
            return 34;
         }
         if ((active1 & 0x800L) != 0L)
            return 35;
         if ((active1 & 0x800000L) != 0L)
            return 3;
         return -1;
      case 1:
         if ((active0 & 0x20000031c0b00L) != 0L)
            return 34;
         if ((active0 & 0x7fdfffffce3f4e0L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 66;
               jjmatchedPos = 1;
            }
            return 34;
         }
         return -1;
      case 2:
         if ((active0 & 0x1a787484002e0L) != 0L)
            return 34;
         if ((active0 & 0x7fc5878b6abf400L) != 0L)
         {
            if (jjmatchedPos != 2)
            {
               jjmatchedKind = 66;
               jjmatchedPos = 2;
            }
            return 34;
         }
         return -1;
      case 3:
         if ((active0 & 0x76c5800b6836400L) != 0L)
         {
            if (jjmatchedPos != 3)
            {
               jjmatchedKind = 66;
               jjmatchedPos = 3;
            }
            return 34;
         }
         if ((active0 & 0x90007800289000L) != 0L)
            return 34;
         if ((active0 & 0x1000000000000L) != 0L)
         {
            if (jjmatchedPos != 3)
            {
               jjmatchedKind = 66;
               jjmatchedPos = 3;
            }
            return 11;
         }
         return -1;
      case 4:
         if ((active0 & 0x244000b2010000L) != 0L)
            return 34;
         if ((active0 & 0x1004000000000L) != 0L)
            return 11;
         if ((active0 & 0x748180004826400L) != 0L)
         {
            jjmatchedKind = 66;
            jjmatchedPos = 4;
            return 34;
         }
         return -1;
      case 5:
         if ((active0 & 0x640000004824000L) != 0L)
            return 34;
         if ((active0 & 0x108180000002400L) != 0L)
         {
            jjmatchedKind = 66;
            jjmatchedPos = 5;
            return 34;
         }
         return -1;
      case 6:
         if ((active0 & 0x100000000002000L) != 0L)
         {
            jjmatchedKind = 66;
            jjmatchedPos = 6;
            return 34;
         }
         if ((active0 & 0x8180000000400L) != 0L)
            return 34;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0, long active1)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 33:
         jjmatchedKind = 89;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x8000L);
      case 35:
         return jjStopAtPos(0, 88);
      case 40:
         return jjStopAtPos(0, 71);
      case 41:
         return jjStopAtPos(0, 72);
      case 42:
         return jjStopAtPos(0, 73);
      case 43:
         return jjStopAtPos(0, 85);
      case 44:
         return jjStopAtPos(0, 83);
      case 45:
         return jjStartNfaWithStates_0(0, 86, 0);
      case 46:
         return jjStartNfaWithStates_0(0, 75, 35);
      case 47:
         return jjStartNfaWithStates_0(0, 87, 3);
      case 58:
         return jjStopAtPos(0, 74);
      case 59:
         return jjStopAtPos(0, 84);
      case 60:
         jjmatchedKind = 77;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x60000L);
      case 61:
         return jjStopAtPos(0, 78);
      case 62:
         jjmatchedKind = 76;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x10000L);
      case 65:
      case 97:
         return jjMoveStringLiteralDfa1_0(0x478400003e0L, 0x0L);
      case 66:
      case 98:
         return jjMoveStringLiteralDfa1_0(0xc00L, 0x0L);
      case 67:
      case 99:
         return jjMoveStringLiteralDfa1_0(0x88080000000L, 0x0L);
      case 68:
      case 100:
         return jjMoveStringLiteralDfa1_0(0x100000003000L, 0x0L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa1_0(0x200000004000L, 0x0L);
      case 70:
      case 102:
         return jjMoveStringLiteralDfa1_0(0x400000008000L, 0x0L);
      case 71:
      case 103:
         return jjMoveStringLiteralDfa1_0(0x10000L, 0x0L);
      case 72:
      case 104:
         return jjMoveStringLiteralDfa1_0(0x20000L, 0x0L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa1_0(0x1c0000L, 0x0L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa1_0(0x1800000200000L, 0x0L);
      case 77:
      case 109:
         return jjMoveStringLiteralDfa1_0(0x300000000L, 0x0L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa1_0(0xc00000L, 0x0L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa1_0(0x3000000L, 0x0L);
      case 80:
      case 112:
         return jjMoveStringLiteralDfa1_0(0x6000000000000L, 0x0L);
      case 82:
      case 114:
         return jjMoveStringLiteralDfa1_0(0x238000000000000L, 0x0L);
      case 83:
      case 115:
         return jjMoveStringLiteralDfa1_0(0xc0010404000000L, 0x0L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa1_0(0x100020008000000L, 0x0L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa1_0(0x10000000L, 0x0L);
      case 87:
      case 119:
         return jjMoveStringLiteralDfa1_0(0x20000000L, 0x0L);
      case 88:
      case 120:
         return jjMoveStringLiteralDfa1_0(0x400000000000000L, 0x0L);
      default :
         return jjMoveNfa_0(2, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0, long active1)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0, active1);
      return 1;
   }
   switch(curChar)
   {
      case 61:
         if ((active1 & 0x8000L) != 0L)
            return jjStopAtPos(1, 79);
         else if ((active1 & 0x10000L) != 0L)
            return jjStopAtPos(1, 80);
         else if ((active1 & 0x20000L) != 0L)
            return jjStopAtPos(1, 81);
         break;
      case 62:
         if ((active1 & 0x40000L) != 0L)
            return jjStopAtPos(1, 82);
         break;
      case 65:
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x18020100020000L, active1, 0L);
      case 66:
      case 98:
         return jjMoveStringLiteralDfa2_0(active0, 0x40000000000L, active1, 0L);
      case 67:
      case 99:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000000000L, active1, 0L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x200180004001400L, active1, 0L);
      case 72:
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x20000000L, active1, 0L);
      case 73:
      case 105:
         if ((active0 & 0x2000000000000L) != 0L)
            return jjStartNfaWithStates_0(1, 49, 34);
         return jjMoveStringLiteralDfa2_0(active0, 0x10200202000L, active1, 0L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x400000000020L, active1, 0L);
      case 77:
      case 109:
         return jjMoveStringLiteralDfa2_0(active0, 0x400000000000000L, active1, 0L);
      case 78:
      case 110:
         if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 18;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x100800c0L, active1, 0L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x25808088c00000L, active1, 0L);
      case 81:
      case 113:
         return jjMoveStringLiteralDfa2_0(active0, 0xc0000000000000L, active1, 0L);
      case 82:
      case 114:
         if ((active0 & 0x1000000L) != 0L)
         {
            jjmatchedKind = 24;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x100000002018000L, active1, 0L);
      case 83:
      case 115:
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 8;
            jjmatchedPos = 1;
         }
         else if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(1, 20, 34);
         return jjMoveStringLiteralDfa2_0(active0, 0x800000200L, active1, 0L);
      case 84:
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0x6000000000L, active1, 0L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x400000000L, active1, 0L);
      case 86:
      case 118:
         return jjMoveStringLiteralDfa2_0(active0, 0x40000000L, active1, 0L);
      case 88:
      case 120:
         return jjMoveStringLiteralDfa2_0(active0, 0x200000004000L, active1, 0L);
      case 89:
      case 121:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(1, 11, 34);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0, active1);
}
private final int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1)
{
   if (((active0 &= old0) | (active1 &= old1)) == 0L)
      return jjStartNfa_0(0, old0, old1); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0, 0L);
      return 2;
   }
   switch(curChar)
   {
      case 65:
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x400006000000000L);
      case 67:
      case 99:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(2, 9, 34);
         break;
      case 68:
      case 100:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(2, 6, 34);
         return jjMoveStringLiteralDfa3_0(active0, 0x8000002000000L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x20000000L);
      case 71:
      case 103:
         if ((active0 & 0x40000000L) != 0L)
            return jjStartNfaWithStates_0(2, 30, 34);
         else if ((active0 & 0x800000000000L) != 0L)
         {
            jjmatchedKind = 47;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0x201100000000000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x80810004000L);
      case 75:
      case 107:
         return jjMoveStringLiteralDfa3_0(active0, 0x200000L);
      case 76:
      case 108:
         if ((active0 & 0x20L) != 0L)
            return jjStartNfaWithStates_0(2, 5, 34);
         return jjMoveStringLiteralDfa3_0(active0, 0x4000000L);
      case 77:
      case 109:
         if ((active0 & 0x400000000L) != 0L)
            return jjStartNfaWithStates_0(2, 34, 34);
         break;
      case 78:
      case 110:
         if ((active0 & 0x200000000L) != 0L)
            return jjStartNfaWithStates_0(2, 33, 34);
         else if ((active0 & 0x10000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 40, 34);
         else if ((active0 & 0x20000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 41, 34);
         return jjMoveStringLiteralDfa3_0(active0, 0x10000000000000L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x401000018000L);
      case 80:
      case 112:
         if ((active0 & 0x8000000L) != 0L)
            return jjStartNfaWithStates_0(2, 27, 34);
         else if ((active0 & 0x200000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 45, 34);
         break;
      case 82:
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x80000000000000L);
      case 83:
      case 115:
         if ((active0 & 0x8000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 39, 34);
         else if ((active0 & 0x40000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 42, 34);
         return jjMoveStringLiteralDfa3_0(active0, 0x3000L);
      case 84:
      case 116:
         if ((active0 & 0x400000L) != 0L)
            return jjStartNfaWithStates_0(2, 22, 34);
         return jjMoveStringLiteralDfa3_0(active0, 0x80400L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x160000080000000L);
      case 86:
      case 118:
         return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
      case 87:
      case 119:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000000800000L);
      case 88:
      case 120:
         if ((active0 & 0x100000000L) != 0L)
            return jjStartNfaWithStates_0(2, 32, 34);
         break;
      case 89:
      case 121:
         if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(2, 7, 34);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0, 0L);
}
private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0, 0L);
      return 3;
   }
   switch(curChar)
   {
      case 49:
         return jjMoveStringLiteralDfa4_0(active0, 0x1000000000000L);
      case 65:
      case 97:
         return jjMoveStringLiteralDfa4_0(active0, 0x40000000800000L);
      case 67:
      case 99:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(3, 12, 34);
         break;
      case 68:
      case 100:
         if ((active0 & 0x10000000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 52, 34);
         break;
      case 69:
      case 101:
         if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(3, 21, 34);
         return jjMoveStringLiteralDfa4_0(active0, 0x4000006000000L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x208000000020000L);
      case 76:
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x80000000000L);
      case 77:
      case 109:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(3, 15, 34);
         break;
      case 78:
      case 110:
         if ((active0 & 0x800000000L) != 0L)
            return jjStartNfaWithStates_0(3, 35, 34);
         else if ((active0 & 0x2000000000L) != 0L)
         {
            jjmatchedKind = 37;
            jjmatchedPos = 3;
         }
         return jjMoveStringLiteralDfa4_0(active0, 0x120004080000000L);
      case 79:
      case 111:
         if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(3, 19, 34);
         return jjMoveStringLiteralDfa4_0(active0, 0x400010000000L);
      case 82:
      case 114:
         return jjMoveStringLiteralDfa4_0(active0, 0x100020000000L);
      case 83:
      case 115:
         if ((active0 & 0x1000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 36, 34);
         return jjMoveStringLiteralDfa4_0(active0, 0x4000L);
      case 84:
      case 116:
         if ((active0 & 0x80000000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 55, 34);
         return jjMoveStringLiteralDfa4_0(active0, 0x400000000002000L);
      case 85:
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x10000L);
      case 87:
      case 119:
         return jjMoveStringLiteralDfa4_0(active0, 0x400L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0, 0L);
}
private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0, 0L);
      return 4;
   }
   switch(curChar)
   {
      case 48:
         if ((active0 & 0x1000000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 48, 11);
         break;
      case 50:
         if ((active0 & 0x4000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 38, 11);
         break;
      case 65:
      case 97:
         return jjMoveStringLiteralDfa5_0(active0, 0x8000000000000L);
      case 67:
      case 99:
         return jjMoveStringLiteralDfa5_0(active0, 0x500000004000000L);
      case 68:
      case 100:
         if ((active0 & 0x20000000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 53, 34);
         break;
      case 69:
      case 101:
         if ((active0 & 0x20000000L) != 0L)
            return jjStartNfaWithStates_0(4, 29, 34);
         return jjMoveStringLiteralDfa5_0(active0, 0x100000000400L);
      case 73:
      case 105:
         return jjMoveStringLiteralDfa5_0(active0, 0x80000802000L);
      case 78:
      case 110:
         if ((active0 & 0x10000000L) != 0L)
            return jjStartNfaWithStates_0(4, 28, 34);
         return jjMoveStringLiteralDfa5_0(active0, 0x20000L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa5_0(active0, 0x200000000000000L);
      case 80:
      case 112:
         if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(4, 16, 34);
         break;
      case 82:
      case 114:
         if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(4, 25, 34);
         else if ((active0 & 0x400000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 46, 34);
         else if ((active0 & 0x4000000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 50, 34);
         return jjMoveStringLiteralDfa5_0(active0, 0x40000000000000L);
      case 84:
      case 116:
         if ((active0 & 0x80000000L) != 0L)
            return jjStartNfaWithStates_0(4, 31, 34);
         return jjMoveStringLiteralDfa5_0(active0, 0x4000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0, 0L);
}
private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0, 0L);
      return 5;
   }
   switch(curChar)
   {
      case 65:
      case 97:
         return jjMoveStringLiteralDfa6_0(active0, 0x100000000000000L);
      case 69:
      case 101:
         if ((active0 & 0x40000000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 54, 34);
         return jjMoveStringLiteralDfa6_0(active0, 0x100000000400L);
      case 71:
      case 103:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(5, 17, 34);
         break;
      case 72:
      case 104:
         if ((active0 & 0x400000000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 58, 34);
         break;
      case 78:
      case 110:
         if ((active0 & 0x200000000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 57, 34);
         return jjMoveStringLiteralDfa6_0(active0, 0x8080000002000L);
      case 83:
      case 115:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(5, 14, 34);
         break;
      case 84:
      case 116:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(5, 23, 34);
         else if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(5, 26, 34);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0, 0L);
}
private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0, 0L);
      return 6;
   }
   switch(curChar)
   {
      case 67:
      case 99:
         return jjMoveStringLiteralDfa7_0(active0, 0x2000L);
      case 71:
      case 103:
         if ((active0 & 0x80000000000L) != 0L)
            return jjStartNfaWithStates_0(6, 43, 34);
         break;
      case 78:
      case 110:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(6, 10, 34);
         break;
      case 83:
      case 115:
         if ((active0 & 0x100000000000L) != 0L)
            return jjStartNfaWithStates_0(6, 44, 34);
         else if ((active0 & 0x8000000000000L) != 0L)
            return jjStartNfaWithStates_0(6, 51, 34);
         break;
      case 84:
      case 116:
         return jjMoveStringLiteralDfa7_0(active0, 0x100000000000000L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0, 0L);
}
private final int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0, 0L);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0, 0L);
      return 7;
   }
   switch(curChar)
   {
      case 69:
      case 101:
         if ((active0 & 0x100000000000000L) != 0L)
            return jjStartNfaWithStates_0(7, 56, 34);
         break;
      case 84:
      case 116:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(7, 13, 34);
         break;
      default :
         break;
   }
   return jjStartNfa_0(6, active0, 0L);
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 34;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 34:
               case 11:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 66)
                     kind = 66;
                  jjCheckNAdd(11);
                  break;
               case 35:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 59)
                        kind = 59;
                     jjCheckNAdd(25);
                  }
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 59)
                        kind = 59;
                     jjCheckNAddTwoStates(21, 22);
                  }
                  break;
               case 2:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 60)
                        kind = 60;
                     jjCheckNAddStates(0, 4);
                  }
                  else if (curChar == 46)
                     jjCheckNAddTwoStates(21, 25);
                  else if (curChar == 34)
                     jjCheckNAddTwoStates(18, 19);
                  else if (curChar == 39)
                     jjCheckNAddTwoStates(13, 14);
                  else if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 3;
                  else if (curChar == 45)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 0:
                  if (curChar != 45)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjCheckNAdd(1);
                  break;
               case 1:
                  if ((0xffffffffffffdbffL & l) == 0L)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjCheckNAdd(1);
                  break;
               case 3:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(4, 5);
                  break;
               case 4:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(4, 5);
                  break;
               case 5:
                  if (curChar == 42)
                     jjCheckNAddStates(5, 7);
                  break;
               case 6:
                  if ((0xffff7bffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(7, 5);
                  break;
               case 7:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(7, 5);
                  break;
               case 8:
                  if (curChar == 47 && kind > 65)
                     kind = 65;
                  break;
               case 9:
                  if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 12:
                  if (curChar == 39)
                     jjCheckNAddTwoStates(13, 14);
                  break;
               case 13:
                  if ((0xffffff7fffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(13, 14);
                  break;
               case 14:
                  if (curChar != 39)
                     break;
                  if (kind > 69)
                     kind = 69;
                  jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 15:
                  if (curChar == 39)
                     jjCheckNAddTwoStates(16, 14);
                  break;
               case 16:
                  if ((0xffffff7fffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(16, 14);
                  break;
               case 17:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(18, 19);
                  break;
               case 18:
                  if ((0xfffffffbffffdbffL & l) != 0L)
                     jjCheckNAddTwoStates(18, 19);
                  break;
               case 19:
                  if (curChar == 34 && kind > 70)
                     kind = 70;
                  break;
               case 20:
                  if (curChar == 46)
                     jjCheckNAddTwoStates(21, 25);
                  break;
               case 21:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 59)
                     kind = 59;
                  jjCheckNAddTwoStates(21, 22);
                  break;
               case 23:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(24);
                  break;
               case 24:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 59)
                     kind = 59;
                  jjCheckNAdd(24);
                  break;
               case 25:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 59)
                     kind = 59;
                  jjCheckNAdd(25);
                  break;
               case 26:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 60)
                     kind = 60;
                  jjCheckNAddStates(0, 4);
                  break;
               case 27:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(27, 28);
                  break;
               case 28:
                  if (curChar == 46)
                     jjCheckNAdd(29);
                  break;
               case 29:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 59)
                     kind = 59;
                  jjCheckNAddTwoStates(29, 22);
                  break;
               case 30:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(30, 31);
                  break;
               case 31:
                  if (curChar == 46)
                     jjCheckNAdd(32);
                  break;
               case 32:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 59)
                     kind = 59;
                  jjCheckNAdd(32);
                  break;
               case 33:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 60)
                     kind = 60;
                  jjCheckNAdd(33);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 34:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 66)
                        kind = 66;
                     jjCheckNAdd(11);
                  }
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 66)
                        kind = 66;
                     jjCheckNAddTwoStates(10, 11);
                  }
                  break;
               case 2:
               case 10:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 66)
                     kind = 66;
                  jjCheckNAddTwoStates(10, 11);
                  break;
               case 1:
                  if (kind > 64)
                     kind = 64;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 4:
                  jjCheckNAddTwoStates(4, 5);
                  break;
               case 6:
               case 7:
                  jjCheckNAddTwoStates(7, 5);
                  break;
               case 11:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 66)
                     kind = 66;
                  jjCheckNAdd(11);
                  break;
               case 13:
                  jjCheckNAddTwoStates(13, 14);
                  break;
               case 16:
                  jjCheckNAddTwoStates(16, 14);
                  break;
               case 18:
                  jjAddStates(8, 9);
                  break;
               case 22:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(10, 11);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 64)
                     kind = 64;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 4:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(4, 5);
                  break;
               case 6:
               case 7:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(7, 5);
                  break;
               case 13:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(13, 14);
                  break;
               case 16:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(16, 14);
                  break;
               case 18:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(8, 9);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 34 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   27, 28, 30, 31, 33, 5, 6, 8, 18, 19, 23, 24, 
};
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, "\50", "\51", "\52", "\72", "\56", "\76", "\74", "\75", "\41\75", 
"\76\75", "\74\75", "\74\76", "\54", "\73", "\53", "\55", "\57", "\43", "\41", };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0x1fffffffffffffe1L, 0x3ffffe4L, 
};
static final long[] jjtoSkip = {
   0x1eL, 0x3L, 
};
static final long[] jjtoSpecial = {
   0x0L, 0x3L, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[34];
private final int[] jjstateSet = new int[68];
protected char curChar;
public ADQLParserTokenManager(SimpleCharStream stream)
{
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public ADQLParserTokenManager(SimpleCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 34; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      matchedToken.specialToken = specialToken;
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         matchedToken.specialToken = specialToken;
         return matchedToken;
      }
      else
      {
         if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
         {
            matchedToken = jjFillToken();
            if (specialToken == null)
               specialToken = matchedToken;
            else
            {
               matchedToken.specialToken = specialToken;
               specialToken = (specialToken.next = matchedToken);
            }
         }
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

}
