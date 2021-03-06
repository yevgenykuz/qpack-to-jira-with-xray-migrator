package com.yevgenyk.q2jwxmigrator.utilities;

public class QpackXmlHelper {
    private static final String XML_PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    public static String fixQpackObjectStringTags(String str) {
        return str.replaceAll("<string xmlns=\"http://orcanos.com/\">", "")
                .replace("</string>", "")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }


    public static String fixQpackObjectXmlTagContent(String xml, String tagName) {
        String[] tags = xml.split(String.format("<%s>", tagName));

        for (String tag : tags) {
            int endIndx = tag.indexOf(String.format("</%s>", tagName));
            if (endIndx > 0) {
                String originalname = tag.substring(0, endIndx);
                String normalizedName = originalname.replaceAll(">", "&gt;").replaceAll("<", "&lt;");
                xml = xml.replaceAll(originalname, normalizedName);
            }
        }

        return xml;
    }

    public static String removeQpackWebObjectItemTags(String str) {
        return str.replaceAll("<item>", "")
                .replace("</item>", "");
    }

    public static String surroundQpackWebObjectDescriptionWithTags(String str) {
        String openingPattern = "obj_status_desc=";
        String[] split = str.split(openingPattern, 2);
        String middlePattern = ">";
        String[] secondSplit = split[1].split(middlePattern, 2);
        String closingPattern = "<steps>";
        String[] thirdSplit = secondSplit[1].split(closingPattern, 2);

        // handle TC without steps:
        if (thirdSplit.length < 2) {
            closingPattern = "</object>";
            thirdSplit = secondSplit[1].split(closingPattern, 2);
        }

        return split[0] + openingPattern + secondSplit[0] + middlePattern + "<description>" + thirdSplit[0] +
                "</description>" + closingPattern + thirdSplit[1];
    }

    public static String XMLToString(String xml) {
        return xml.replace("<", "&lt;").replace(">", "&gt;");
    }

    public static String removeXmlPrefix(String xml) {
        if (xml.startsWith(XML_PREFIX)) {
            return xml.substring(XML_PREFIX.length());
        } else {
            return xml;
        }
    }

    public static String removeNamespaces(String xml) {
        int startIndx = xml.indexOf("xmlns");
        if (startIndx > 0) {
            int endIndex = xml.substring(startIndx).indexOf(">") + startIndx;
            String namespace = xml.substring(startIndx, endIndex);
            return xml.replaceAll(namespace, "");
        }

        return xml;
    }

    public static String surroundWithCDATA(String str) {
        return "<![CDATA[" + str + "]]>";
    }
}
