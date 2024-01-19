/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Andre_601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ch.andre601.advancedserverlist.test;

import com.unascribed.flexver.FlexVerComparator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlexVerTests{
    
    private final String baseVersion = "1.0.0";
    private final String versionMinor = "1.0.1";
    private final String versionMajor = "1.1.0";
    private final String versionBoth = "1.1.1";
    
    private final String versionBeta = "1.0.0-b1";
    private final String versionBetaMinor = "1.0.1-b1";
    private final String versionBetaMajor = "1.1.0-b1";
    private final String versionBetaBoth = "1.1.1-b1";
    
    private final String versionBeta2 = "1.0.0-b2";
    private final String versionBeta2Minor = "1.0.1-b2";
    private final String versionBeta2Major = "1.1.0-b2";
    private final String versionBeta2Both = "1.1.1-b2";
    
    @Test
    public void testFlexVerChecks(){
        System.out.println("START: Version comparison Tests");
        
        compareBaseVersion();
        compareMinorVersion();
        compareMajorVersion();
        compareBothVersion();
        
        compareBetaVersion();
        compareBetaMinorVersion();
        compareBetaMajorVersion();
        compareBetaBothVersion();
        
        compareBeta2Version();
        compareBeta2MinorVersion();
        compareBeta2MajorVersion();
        compareBeta2BothVersion();
        
        System.out.println("END: Version comparison Tests");
    }
    
    private void compareBaseVersion(){
        System.out.println("┌────────────────────────────────┐");
        System.out.println("│ Normal Version                 │");
        System.out.println("│                                │");
        compare(baseVersion, "=", baseVersion, 0);
        compare(baseVersion, "<", versionMinor, -1);
        compare(baseVersion, "<", versionMajor, -1);
        compare(baseVersion, "<", versionBoth, -1);
        compare(baseVersion, ">", versionBeta, 1);
        compare(baseVersion, "<", versionBetaMinor, -1);
        compare(baseVersion, "<", versionBetaMajor, -1);
        compare(baseVersion, "<", versionBetaBoth, -1);
        compare(baseVersion, ">", versionBeta2, 1);
        compare(baseVersion, "<", versionBeta2Minor, -1);
        compare(baseVersion, "<", versionBeta2Major, -1);
        compare(baseVersion, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareMinorVersion(){
        System.out.println("│ Minor Version                  │");
        System.out.println("│                                │");
        compare(versionMinor, ">", baseVersion, 1);
        compare(versionMinor, "=", versionMinor, 0);
        compare(versionMinor, "<", versionMajor, -1);
        compare(versionMinor, "<", versionBoth, -1);
        compare(versionMinor, ">", versionBeta, 1);
        compare(versionMinor, ">", versionBetaMinor, 1);
        compare(versionMinor, "<", versionBetaMajor, -1);
        compare(versionMinor, "<", versionBetaBoth, -1);
        compare(versionMinor, ">", versionBeta2, 1);
        compare(versionMinor, ">", versionBeta2Minor, 1);
        compare(versionMinor, "<", versionBeta2Major, -1);
        compare(versionMinor, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareMajorVersion(){
        System.out.println("│ Major Version                  │");
        System.out.println("│                                │");
        compare(versionMajor, ">", baseVersion, 1);
        compare(versionMajor, ">", versionMinor, 1);
        compare(versionMajor, "=", versionMajor, 0);
        compare(versionMajor, "<", versionBoth, -1);
        compare(versionMajor, ">", versionBeta, 1);
        compare(versionMajor, ">", versionBetaMinor, 1);
        compare(versionMajor, ">", versionBetaMajor, 1);
        compare(versionMajor, "<", versionBetaBoth, -1);
        compare(versionMajor, ">", versionBeta2, 1);
        compare(versionMajor, ">", versionBeta2Minor, 1);
        compare(versionMajor, ">", versionBeta2Major, 1);
        compare(versionMajor, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBothVersion(){
        System.out.println("│ Major + Minor Version          │");
        System.out.println("│                                │");
        compare(versionBoth, ">", baseVersion, 1);
        compare(versionBoth, ">", versionMinor, 1);
        compare(versionBoth, ">", versionMajor, 1);
        compare(versionBoth, "=", versionBoth, 0);
        compare(versionBoth, ">", versionBeta, 1);
        compare(versionBoth, ">", versionBetaMinor, 1);
        compare(versionBoth, ">", versionBetaMajor, 1);
        compare(versionBoth, ">", versionBetaBoth, 1);
        compare(versionBoth, ">", versionBeta2, 1);
        compare(versionBoth, ">", versionBeta2Minor, 1);
        compare(versionBoth, ">", versionBeta2Major, 1);
        compare(versionBoth, ">", versionBeta2Both, 1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBetaVersion(){
        System.out.println("│ Beta Version                   │");
        System.out.println("│                                │");
        compare(versionBeta, "<", baseVersion, -1);
        compare(versionBeta, "<", versionMinor, -1);
        compare(versionBeta, "<", versionMajor, -1);
        compare(versionBeta, "<", versionBoth, -1);
        compare(versionBeta, "=", versionBeta, 0);
        compare(versionBeta, "<", versionBetaMinor, -1);
        compare(versionBeta, "<", versionBetaMajor, -1);
        compare(versionBeta, "<", versionBetaBoth, -1);
        compare(versionBeta, "<", versionBeta2, -1);
        compare(versionBeta, "<", versionBeta2Minor, -1);
        compare(versionBeta, "<", versionBeta2Major, -1);
        compare(versionBeta, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBetaMinorVersion(){
        System.out.println("│ Beta Version Minor             │");
        System.out.println("│                                │");
        compare(versionBetaMinor, ">", baseVersion, 1);
        compare(versionBetaMinor, "<", versionMinor, -1);
        compare(versionBetaMinor, "<", versionMajor, -1);
        compare(versionBetaMinor, "<", versionBoth, -1);
        compare(versionBetaMinor, ">", versionBeta, 1);
        compare(versionBetaMinor, "=", versionBetaMinor, 0);
        compare(versionBetaMinor, "<", versionBetaMajor, -1);
        compare(versionBetaMinor, "<", versionBetaBoth, -1);
        compare(versionBetaMinor, ">", versionBeta2, 1);
        compare(versionBetaMinor, "<", versionBeta2Minor, -1);
        compare(versionBetaMinor, "<", versionBeta2Major, -1);
        compare(versionBetaMinor, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBetaMajorVersion(){
        System.out.println("│ Beta Version Major             │");
        System.out.println("│                                │");
        compare(versionBetaMajor, ">", baseVersion, 1);
        compare(versionBetaMajor, ">", versionMinor, 1);
        compare(versionBetaMajor, "<", versionMajor, -1);
        compare(versionBetaMajor, "<", versionBoth, -1);
        compare(versionBetaMajor, ">", versionBeta, 1);
        compare(versionBetaMajor, ">", versionBetaMinor, 1);
        compare(versionBetaMajor, "=", versionBetaMajor, 0);
        compare(versionBetaMajor, "<", versionBetaBoth, -1);
        compare(versionBetaMajor, ">", versionBeta2, 1);
        compare(versionBetaMajor, ">", versionBeta2Minor, 1);
        compare(versionBetaMajor, "<", versionBeta2Major, -1);
        compare(versionBetaMajor, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBetaBothVersion(){
        System.out.println("│ Beta Version Major + Minor     │");
        System.out.println("│                                │");
        compare(versionBetaBoth, ">", baseVersion, 1);
        compare(versionBetaBoth, ">", versionMinor, 1);
        compare(versionBetaBoth, ">", versionMajor, 1);
        compare(versionBetaBoth, "<", versionBoth, -1);
        compare(versionBetaBoth, ">", versionBeta, 1);
        compare(versionBetaBoth, ">", versionBetaMinor, 1);
        compare(versionBetaBoth, ">", versionBetaMajor, 1);
        compare(versionBetaBoth, "=", versionBetaBoth, 0);
        compare(versionBetaBoth, ">", versionBeta2, 1);
        compare(versionBetaBoth, ">", versionBeta2Minor, 1);
        compare(versionBetaBoth, ">", versionBeta2Major, 1);
        compare(versionBetaBoth, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBeta2Version(){
        System.out.println("│ Beta Version 2                 │");
        System.out.println("│                                │");
        compare(versionBeta2, "<", baseVersion, -1);
        compare(versionBeta2, "<", versionMinor, -1);
        compare(versionBeta2, "<", versionMajor, -1);
        compare(versionBeta2, "<", versionBoth, -1);
        compare(versionBeta2, ">", versionBeta, 1);
        compare(versionBeta2, "<", versionBetaMinor, -1);
        compare(versionBeta2, "<", versionBetaMajor, -1);
        compare(versionBeta2, "<", versionBetaBoth, -1);
        compare(versionBeta2, "=", versionBeta2, 0);
        compare(versionBeta2, "<", versionBeta2Minor, -1);
        compare(versionBeta2, "<", versionBeta2Major, -1);
        compare(versionBeta2, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBeta2MinorVersion(){
        System.out.println("│ Beta Version 2 Minor           │");
        System.out.println("│                                │");
        compare(versionBeta2Minor, ">", baseVersion, 1);
        compare(versionBeta2Minor, "<", versionMinor, -1);
        compare(versionBeta2Minor, "<", versionMajor, -1);
        compare(versionBeta2Minor, "<", versionBoth, -1);
        compare(versionBeta2Minor, ">", versionBeta, 1);
        compare(versionBeta2Minor, ">", versionBetaMinor, 1);
        compare(versionBeta2Minor, "<", versionBetaMajor, -1);
        compare(versionBeta2Minor, "<", versionBetaBoth, -1);
        compare(versionBeta2Minor, ">", versionBeta2, 1);
        compare(versionBeta2Minor, "=", versionBeta2Minor, 0);
        compare(versionBeta2Minor, "<", versionBeta2Major, -1);
        compare(versionBeta2Minor, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBeta2MajorVersion(){
        System.out.println("│ Beta Version 2 Major           │");
        System.out.println("│                                │");
        compare(versionBeta2Major, ">", baseVersion, 1);
        compare(versionBeta2Major, ">", versionMinor, 1);
        compare(versionBeta2Major, "<", versionMajor, -1);
        compare(versionBeta2Major, "<", versionBoth, -1);
        compare(versionBeta2Major, ">", versionBeta, 1);
        compare(versionBeta2Major, ">", versionBetaMinor, 1);
        compare(versionBeta2Major, ">", versionBetaMajor, 1);
        compare(versionBeta2Major, "<", versionBetaBoth, -1);
        compare(versionBeta2Major, ">", versionBeta2, 1);
        compare(versionBeta2Major, ">", versionBeta2Minor, 1);
        compare(versionBeta2Major, "=", versionBeta2Major, 0);
        compare(versionBeta2Major, "<", versionBeta2Both, -1);
        System.out.println("├────────────────────────────────┤");
    }
    
    private void compareBeta2BothVersion(){
        System.out.println("│ Beta Version 2 Major + Minor   │");
        System.out.println("│                                │");
        compare(versionBeta2Both, ">", baseVersion, 1);
        compare(versionBeta2Both, ">", versionMinor, 1);
        compare(versionBeta2Both, ">", versionMajor, 1);
        compare(versionBeta2Both, "<", versionBoth, -1);
        compare(versionBeta2Both, ">", versionBeta, 1);
        compare(versionBeta2Both, ">", versionBetaMinor, 1);
        compare(versionBeta2Both, ">", versionBetaMajor, 1);
        compare(versionBeta2Both, ">", versionBetaBoth, 1);
        compare(versionBeta2Both, ">", versionBeta2, 1);
        compare(versionBeta2Both, ">", versionBeta2Minor, 1);
        compare(versionBeta2Both, ">", versionBeta2Major, 1);
        compare(versionBeta2Both, "=", versionBeta2Both, 0);
        System.out.println("└────────────────────────────────┘");
    }
    
    private void compare(String version1, String comparator, String version2, int expected){
        String expression = version1 + " " + comparator + " " + version2 + "?";
        int result = FlexVerComparator.compare(version1, version2);
        
        System.out.printf("│ %-24s %5b │\n", expression, FlexVerComparator.compare(version1, version2) == expected);
        assertEquals(result, expected);
    }
}
