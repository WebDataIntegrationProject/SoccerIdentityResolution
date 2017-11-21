package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.comparators;

import junit.framework.Assert;
import junit.framework.TestCase;

public class StringSimplifierTest extends TestCase {

    public void testSimplifyString() throws Exception {

        // tests lowercasing, umlauts, parentheses deletion, symbol deletion
        Assert.assertEquals(StringSimplifier.simplifyString("1. F.C. Köln (football club)"), "1 fc koeln");

        // tests lowercasing, umlauts, parentheses deletion, symbol deletion, trailing & leading space deletion
        Assert.assertEquals(StringSimplifier.simplifyString("  1. F.C. Köln (football club) FC  "), "1 fc koeln fc");

        // tests lowercasing, accent stripping
        Assert.assertEquals(StringSimplifier.simplifyString("Liberté"), "liberte");

        // test for special characters
        Assert.assertEquals(StringSimplifier.simplifyString("ç"), "c");
    }

    public void testSimplifyStringClubOptimized(){
        Assert.assertEquals(StringSimplifier.simplifyStringClubOptimized("FC Liberté"), "liberte");
        Assert.assertEquals(StringSimplifier.simplifyStringClubOptimized("Liberté FC"), "liberte");
        Assert.assertEquals(StringSimplifier.simplifyStringClubOptimized("Libertéfc FC"), "libertefc");
        Assert.assertEquals(StringSimplifier.simplifyStringClubOptimized("fcLibertéfc FC"), "fclibertefc");

    }

}