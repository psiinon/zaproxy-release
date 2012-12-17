PK
    �K�A            	  META-INF/��  PK
    �K�A�?�~g   g      META-INF/MANIFEST.MFManifest-Version: 1.0
Ant-Version: Apache Ant 1.8.2
Created-By: 1.7.0_04-b22 (Oracle Corporation)

PK
    �K�A               org/PK
    �K�A               org/zaproxy/PK
    �K�A               org/zaproxy/zap/PK
    �K�A               org/zaproxy/zap/extension/PK
    �K�A            %   org/zaproxy/zap/extension/ascanrules/PK
    �K�Au$�  �  A   org/zaproxy/zap/extension/ascanrules/TestClientBrowserCache.class����   3 {
  = > ? @ A
  B
 C D
 E F
 E G
 C H
 I J
 C K
 L M
 N O P	  Q
  R S	  T
  U V
  W X
 Y Z [ \ ] patternNoCache Ljava/util/regex/Pattern; patternHtmlNoCache <init> ()V Code LineNumberTable LocalVariableTable this =Lorg/zaproxy/zap/extension/ascanrules/TestClientBrowserCache; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription getCategory getSolution msg Ljava/lang/String; getReference init scan *Lorg/parosproxy/paros/network/HttpMessage; result Z StackMapTable ^ getRisk <clinit> 
SourceFile TestClientBrowserCache.java    Secure page browser cache �Secure page can be cached in browser.  Cache control is not set in HTTP header nor HTML header.  Sensitive content can be recovered from browser storage.-The best way is to set HTTP header with: 'Pragma: No-cache' and 'Cache-control: No-cache'.
Alternatively, this can be set in the HTML header by:
<META HTTP-EQUIV='Pragma' CONTENT='no-cache'>
<META HTTP-EQUIV='Cache-Control' CONTENT='no-cache'>
but some browsers may have problem using this method. �. How to prevent caching in Internet Explorer - http://support.microsoft.com/default.aspx?kbid=234067
. Pragma: No-cache Tag May Not Prevent Page from Being Cached - http://support.microsoft.com/default.aspx?kbid=222064 _ ` ^ a b c d e f e g h i j ' k l m n ' o p q Cache-control   r s Pragma   t u   v w \QNo-cache\E|\QNo-store\E x y z J<META[^>]+(Pragma|\QCache-Control\E)[^>]+(\QNo-cache\E|\QNo-store\E)[^>]*> ;org/zaproxy/zap/extension/ascanrules/TestClientBrowserCache 3org/parosproxy/paros/core/scanner/AbstractAppPlugin (org/parosproxy/paros/network/HttpMessage 
getBaseMsg ,()Lorg/parosproxy/paros/network/HttpMessage; getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader 	getSecure ()Z isImage getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody length getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader getStatusCode +org/parosproxy/paros/network/HttpStatusCode isClientError (I)Z matchHeaderPattern X(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/util/regex/Pattern;)Z matchBodyPattern _(Lorg/parosproxy/paros/network/HttpMessage;Ljava/util/regex/Pattern;Ljava/lang/StringBuilder;)Z bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V java/util/regex/Pattern compile .(Ljava/lang/String;I)Ljava/util/regex/Pattern; !                      !   /     *� �    "       * #        $ %    & '  !   .     '�    "       8 #        $ %    ( )  !   -     �    "       @ #        $ %    * +  !   ,     �    "       H #        $ %    , )  !   -     �    "       P #        $ %    - '  !   ,     �    "       X #        $ %    . )  !   =     L+�    "   
    `  e #        $ %     / 0   1 )  !   =     L+�    "   
    m  o #        $ %     / 0   2    !   +      �    "       x #        $ %    3    !   �     p*� L=+� � � �+� � 	� �+� 
� � �+� � � � �*+� � � *+� � � *+� � � =� *+� �    "   >    �  �  �  �  �  �  � ' � ( � 5 � 6 � \ � ^ � b � o � #        p $ %    k / 4   i 5 6  7    �  8

'  9 '  !   ,     �    "       � #        $ %    :    !   1      
� � 
� � �    "   
    - 
 1  ;    <PK
    �K�A^��l
#  
#  @   org/zaproxy/zap/extension/ascanrules/TestCrossSiteScriptV2.class����   3M
 W �  �L	 � � �
 � �	 U �
 � � �
 � � � �
  �
 � � � � � � � � �
  �
  �
  �
  � �
 � �
 U �
 U � �	 U �
  �
 � � �
  �	 � �
 U �
 � �
  �
  �
 � � � �
  �
  � �
 U � � � �
 , � �
 , �
 U �
 , �
 , � � �
 � �
 � �
  �
 � �
 , � � �
 , �
 , � � � � � � � �
 , � � � �
 , �
  � � � � � � � � � �
 � � �
 � � � vuln %Lorg/zaproxy/zap/model/Vulnerability; log Lorg/apache/log4j/Logger; <init> ()V Code LineNumberTable LocalVariableTable this <Lorg/zaproxy/zap/extension/ascanrules/TestCrossSiteScriptV2; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription StackMapTable getCategory getSolution getReference ref Ljava/lang/String; i$ Ljava/util/Iterator; sb Ljava/lang/StringBuilder; � � � init performAttack �(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;Lorg/zaproxy/zap/httputils/HtmlContext;I)Ljava/util/List; e Ljava/lang/Exception; msg *Lorg/parosproxy/paros/network/HttpMessage; param attack targetContext 'Lorg/zaproxy/zap/httputils/HtmlContext; ignoreFlags I msg2 hca /Lorg/zaproxy/zap/httputils/HtmlContextAnalyser; � � � � � 	Signature �(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;Lorg/zaproxy/zap/httputils/HtmlContext;I)Ljava/util/List<Lorg/zaproxy/zap/httputils/HtmlContext;>; scan Q(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)V 	contexts2 Ljava/util/List; context2 context attackWorked Z contexts value LocalVariableTypeTable 9Ljava/util/List<Lorg/zaproxy/zap/httputils/HtmlContext;>;  getRisk <clinit> 
SourceFile TestCrossSiteScriptV2.java \ ] scanner.plugin.xss X Y i f 2Failed to load vulnerability description from file l f /Failed to load vulnerability solution from file java/lang/StringBuilder	 
 � java/lang/String d f 0Failed to load vulnerability reference from file � java/lang/Exception Z [ f -org/zaproxy/zap/httputils/HtmlContextAnalyser \ #$%&'()*)+, f- d. f/ f '"<script>alert(1);</script> x y01 %org/zaproxy/zap/httputils/HtmlContext2 f  3456 f7 	;alert(1) +Failed to find vuln in script attribute on 89:;<=>?@ javascript:alert(1); (Failed to find vuln in url attribute on AB f  src=http://badsite.com 1Failed to find vuln in tag with src attribute on  ><script>alert(1);</script> .Failed to find vuln with simple script attack   onMouseOver= 	alert(1); 0Failed to find vuln in with simple onmounseover C !--><script>alert(1);</script><!-- (--><b onMouseOver=alert(1);>test</b><!-- bodyD fEF <script>alert(1);</script> !<b onMouseOver=alert(1);>test</b> script TBI Body tag </ ><script>alert(1);</script>< > 
;alert(1); wasc_8GHI :org/zaproxy/zap/extension/ascanrules/TestCrossSiteScriptV2JK 8org/parosproxy/paros/core/scanner/AbstractAppParamPlugin java/util/Iterator (org/parosproxy/paros/network/HttpMessage java/util/List org/parosproxy/paros/Constant messages Lorg/zaproxy/zap/utils/I18N; org/zaproxy/zap/utils/I18N 	getString &(Ljava/lang/String;)Ljava/lang/String; #org/zaproxy/zap/model/Vulnerability getReferences ()Ljava/util/List; iterator ()Ljava/util/Iterator; hasNext ()Z next ()Ljava/lang/Object; length append (C)Ljava/lang/StringBuilder; -(Ljava/lang/String;)Ljava/lang/StringBuilder; toString cloneRequest ,()Lorg/parosproxy/paros/network/HttpMessage; setParameter b(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; sendAndReceive -(Lorg/parosproxy/paros/network/HttpMessage;)V 
getMessage org/apache/log4j/Logger error *(Ljava/lang/Object;Ljava/lang/Throwable;)VL 7org/parosproxy/paros/core/scanner/Plugin$AlertThreshold AlertThreshold InnerClasses HIGH 9Lorg/parosproxy/paros/core/scanner/Plugin$AlertThreshold; getAlertThreshold ;()Lorg/parosproxy/paros/core/scanner/Plugin$AlertThreshold; equals (Ljava/lang/Object;)Z getHtmlContexts L(Ljava/lang/String;Lorg/zaproxy/zap/httputils/HtmlContext;I)Ljava/util/List; $(Ljava/lang/String;)Ljava/util/List; getEyeCatcher size toLowerCase toUpperCase get (I)Ljava/lang/Object; 	getTarget getMsg bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V getTagAttribute isInScriptAttribute getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; -(Ljava/lang/Object;)Ljava/lang/StringBuilder; debug (Ljava/lang/Object;)V isInUrlAttribute isInTagWithSrc getSurroundingQuote isHtmlComment getParentTag equalsIgnoreCase (Ljava/lang/String;)Z %org/zaproxy/zap/model/Vulnerabilities getVulnerability 9(Ljava/lang/String;)Lorg/zaproxy/zap/model/Vulnerability; 	getLogger ,(Ljava/lang/Class;)Lorg/apache/log4j/Logger; (org/parosproxy/paros/core/scanner/Plugin ! U W    
 X Y   
 Z [     \ ]  ^   /     *� �    _       " `        a b    c d  ^   -     �    _       ) `        a b    e f  ^   3     	� � �    _       . `       	 a b    g h  ^   ,     �    _       3 `        a b    i f  ^   K     � � 
� � ��    _       8  9  ; `        a b   j      k d  ^   ,     �    _       @ `        a b    l f  ^   K     � � 
� � 	�
�    _       E  F  H `        a b   j      m f  ^   �     L� � F� Y� L� � �  M,�  � $,�  � N+� � 
+
� W+-� W���+� ��    _   & 	   M  N  O - P 4 Q ; S A T D U I W `   *  -  n o   * p q   ; r s    L a b   j    �  t u�   v� �   w ]  ^   +      �    _       ] `        a b    x y  ^  #     Q+� :*,-� W*� � :� � � � Y� :�  *� !� "� -� #�-� $�       _   * 
   a  b  d  g  e  f ' i 2 j ? l J n `   \ 	   z {    Q a b     Q | }    Q ~ o    Q  o    Q � �    Q � �   K � }  2  � �  j   & �   � � v v � �  �� " � �    �  � �  ^  	� 	   �6*+,� %� W*+� � Y+� :� %� #:� & � � %� '� #:� & � � %� (� #:� & � J*+,� Y� ,� � %� � � W*+� � Y+� :� Y� -� � %� � � #:� & � B*+,)� *:� & � ,*,� + � ,� -.� + � ,� /� 06�  :�  ���  � ,:� ��� 1��� 2� �*+,3� *:		�  :

�  � <
�  � ,:� 1� %� 2� *,� -.� /� 06� ���� "� � Y� 4� +� 5� 6� 7� � 8� o� 9� g*+,:� *:		� & � ,*,	� + � ,� -.	� + � ,� /� 06� "� � Y� ;� +� 5� 6� 7� � 8� �� <� |*+,� Y� � =� >� � � *:		� & � ,*,	� + � ,� -.	� + � ,� /� 06� "� � Y� ?� +� 5� 6� 7� � 8� |*+,� Y� � =� @� � � *:		� & � ,*,	� + � ,� -.	� + � ,� /� 06� "� � Y� A� +� 5� 6� 7� � 8��*+,� Y� � =� B� � =� C� � � *:		� & � ,*,	� + � ,� -.	� + � ,� /� 06� "� � Y� D� +� 5� 6� 7� � 8�7� E� �*+,F@� *:		� & � /*,	� + � ,� -.	� + � ,� /� 06� D*+,G@� *:		� & � ,*,	� + � ,� -.	� + � ,� /� 06��H� I� J� �*+,K� *:		� & � /*,	� + � ,� -.	� + � ,� /� 06� x*+,L� *:		�  :

�  � X
�  � ,:H� I� J� M� I� J� /*,	� + � ,� -N	� + � ,� /� 06� ���� �� I� �*+,� Y� O� � I� P� � I� Q� � � *:		� & � /*,	� + � ,� -.	� + � ,� /� 06� mM� I� J� `*+,� Y� � =� R� � =� � � *:		� & � ,*,	� + � ,� -.	� + � ,� /� 06�� � :� � � �   ��   _  � c   v  w  x  z  { ( | 2 ~ A � K � Z � d �  � � � � � � � � � � � � � � � � � � � �# �+ �8 �W �g �{ �~ �� �� �� �� �� �� �� �� �� �� � �$ �F �P �v �y �~ �� �� �� �� �� �� �� � �  �O �Y � �� �� �� �� �� �� �� �� �� � � �3 �6 9FR\	���������= G"m$s%�'�)�+�-�2�5�3�4�6 `   �  � 3 � � W * � � A C p q 
8 p � � 	� W � � 	F W � � 	� W � � 	O W � � 	� w � � 	� F � � � _ p q 
R � � � 	= � � � 	� � �  �� p q  � � �  � � �  (� � � �  z {   � a b    � | }   � ~ o   � � o  �   f 
 � 3 � � 8 p � � 	� W � � 	F W � � 	� W � � 	O W � � 	� w � � 	R � � � 	= � � � 	 (� � �  j   � "� A � �� P� H�  u�  �� % � u?� � #� G �� #� a �� #� Y �� #� f �� #� K �� @� N ��  u� / �� +� � r �� i�   � � v v  B �  � d  ^   ,     �    _      : `        a b    � ]  ^   .      S� T�  U� V� �    _   
    $  %  �    �"   
  �!@PK
    �K�Ar���]  ]  @   org/zaproxy/zap/extension/ascanrules/TestDirectoryBrowsing.class����   3 �
 ) ] ^ _ ` a
 b c
 d e
  f
  g h
 i j k
  ]
  l
  g m
  n
 d o
 ( p
 ( q
 ( r
 ( s
 b t
 u v	 ( w
 ( x	 ( y	 ( z	 ( {	 ( | } ~
 (  �
 � � � � � � � � 
patternIIS Ljava/util/regex/Pattern; patternApache patternGeneralDir1 patternGeneralDir2 patternGeneralParent <init> ()V Code LineNumberTable LocalVariableTable this <Lorg/zaproxy/zap/extension/ascanrules/TestDirectoryBrowsing; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription getCategory getSolution getReference ref Ljava/lang/String; init checkIfDirectory -(Lorg/parosproxy/paros/network/HttpMessage;)V msg *Lorg/parosproxy/paros/network/HttpMessage; uri #Lorg/apache/commons/httpclient/URI; sUri StackMapTable m � 
Exceptions � scan e Ljava/io/IOException; result Z reliability I � } getRisk <clinit> 
SourceFile TestDirectoryBrowsing.java 0 1 Directory browsing �It is possible to view the directory listing.  Directory listing may reveal hidden scripts, include files , backup source files etc which be accessed to read sensitive information. cDisable directory browsing.  If this is required, make sure the listed files does not induce risks.;For IIS, turn off directory browsing.
For Apache, use the 'Options -Indexes' directive to disable indexes in directory or via .htaccess:
. http://httpd.apache.org/docs/mod/core.html#options
. http://alamo.satlug.org/pipermail/satlug/2002-February/000053.html
. or create a default index.html for each directory. � � � � � � � � � : / � � � java/lang/StringBuilder � � !org/apache/commons/httpclient/URI 0 � � � � � D E � � � E � � � � 8 * + � � , + / + - + . + java/io/IOException   � � Parent Directory � � � &\bDirectory Listing\b.*(Tomcat|Apache) \bDirectory\b [\s<]+IMG\s*= Parent directory :org/zaproxy/zap/extension/ascanrules/TestDirectoryBrowsing 3org/parosproxy/paros/core/scanner/AbstractAppPlugin java/lang/String *org/apache/commons/httpclient/URIException (org/parosproxy/paros/network/HttpMessage getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; setQuery (Ljava/lang/String;)V toString endsWith (Ljava/lang/String;)Z append -(Ljava/lang/String;)Ljava/lang/StringBuilder; (Ljava/lang/String;Z)V setURI &(Lorg/apache/commons/httpclient/URI;)V 	getNewMsg ,()Lorg/parosproxy/paros/network/HttpMessage; writeProgress sendAndReceive getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader getStatusCode matchBodyPattern _(Lorg/parosproxy/paros/network/HttpMessage;Ljava/util/regex/Pattern;Ljava/lang/StringBuilder;)Z bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V java/util/regex/Pattern compile .(Ljava/lang/String;I)Ljava/util/regex/Pattern; ! ( )     * +    , +    - +    . +    / +     0 1  2   /     *� �    3       - 4        5 6    7 8  2   ,     �    3       ; 4        5 6    9 :  2   -     �    3       A 4        5 6    ; <  2   ,     �    3       H 4        5 6    = :  2   -     �    3       M 4        5 6    > 8  2   ,     �    3       R 4        5 6    ? :  2   -     �    3       W 4        5 6    @ :  2   =     L+�    3   
    \  a 4        5 6     A B   C 1  2   +      �    3       f 4        5 6    D E  2   �     @+� � M,� ,� 	N-
� � � Y� -� 
� � N+� � Y-� � �    3       k  l  m  n  o / q ? s 4   *    @ 5 6     @ F G   8 H I   . J B  K    � / L M N     O  P 1  2  y     �<*� M>*,� *,� � � 	� *,� ,� �  ȟ �*,� � � <� C*,� � � <� 2*,� � � 
<>� *,� � � *,� � � <>� :� *,� � � 	   ,� !�  	 . �  / � �   3   b    x  y  z 	 }  ~   ! � . � / � ; � @ � L � Q � ] � _ � d � p � | � ~ � � � � � � � � � � � 4   4  �   Q R    � 5 6    � S T   � F G  	 � U V  K    � / WB X  Y 8  2   ,     �    3       � 4        5 6    Z 1  2   [      3"
� #� $
� #� %
� #� &
� #� '
� #� �    3       0 
 1  4  5 ( 6  [    \PK
    �K�A̸!ȱ  �  ?   org/zaproxy/zap/extension/ascanrules/TestExternalRedirect.class����   3 �
  K L	  M N	  O P Q R S
  T
  U
 V W
 X Y Z
 X [
 \ ]
  ^
  _ `
  a
  b c
  d
  e
 \ f
  g h i 	redirect1 Ljava/lang/String; 	redirect2 <init> ()V Code LineNumberTable LocalVariableTable this ;Lorg/zaproxy/zap/extension/ascanrules/TestExternalRedirect; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription msg getCategory getSolution getReference init scan Q(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)V e Ljava/lang/Exception; *Lorg/parosproxy/paros/network/HttpMessage; param value locationHeader locationHeader2 redirect uri #Lorg/apache/commons/httpclient/URI; StackMapTable h j k ` c checkResult Q(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)Z attack getRisk 
SourceFile TestExternalRedirect.java   ! http://www.owasp.org   www.owasp.org   External redirect �Arbitrary external redirection can be.  A phishing email can make use of this to entice readers to click on the link to redirect readers to bogus sites. hOnly allow redirection within the same web sites; or only allow redirection to designated external URLs.   l m n o j p q r s ( Location t u k v w x u y u !org/apache/commons/httpclient/URI   z { * java/lang/Exception | } E F ~  � � 9org/zaproxy/zap/extension/ascanrules/TestExternalRedirect 8org/parosproxy/paros/core/scanner/AbstractAppParamPlugin (org/parosproxy/paros/network/HttpMessage java/lang/String 	getNewMsg ,()Lorg/parosproxy/paros/network/HttpMessage; sendAndReceive .(Lorg/parosproxy/paros/network/HttpMessage;Z)V getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader getStatusCode 	getHeader &(Ljava/lang/String;)Ljava/lang/String; compareToIgnoreCase (Ljava/lang/String;)I getURLDecode getURLEncode (Ljava/lang/String;Z)V getPathQuery setParameter b(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; 
startsWith (Ljava/lang/String;)Z bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V !                    !  "   C     *� *� *� �    #       #  & 
 ' $        % &    ' (  "   .     u0�    #       . $        % &    ) *  "   -     �    #       6 $        % &    + ,  "   ,     �    #       @ $        % &    - *  "   =     L+�    #   
    I  J $        % &     .    / (  "   ,     �    #       R $        % &    0 *  "   -     �    #       Z $        % &    1 *  "   -     	�    #       c $        % &    2 !  "   +      �    #       l $        % &    3 4  "  ;  	   �::	::*� 
L*+� +� � -� +� � .� �+� � :� �-� � *� :� -� � � *� � :� =� Y� :� :-� � *� :� -� � � *� � :� �� :*� 
L*+,� W*+� *+,� � �� :�   2 �  3 C �  D � �  � � �   #   � "   q  r  s 
 u  w  y  z 2 } 3 � > � C � D � M � V � b � k � p � | � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � $   f 
 �   5 6  �   5 6    � % &     � . 7    � 8     � 9    � :    � ;   
 � <    � = >  ?   2 � 3  @ A B B B B B C  )B DB D  E F  "   �     H+� � -� +� � .� �+� � :� *� � � *,-	+� ��    #       �  �  � ' � 8 � D � F � $   4    H % &     H . 7    H 8     H G   ' ! :   ?   	 � ) B  H (  "   ,     �    #       � $        % &    I    JPK
    �K�A�i}�D  D  K   org/zaproxy/zap/extension/ascanrules/TestInfoPrivateAddressDisclosure.class����   3 t
  D E F G
  H
 I J
 K L	  M
 N O P
 
 D
 Q R
 Q S
 
 T U
 
 V W
 
 L
  X Y
 N Z [ \ REGULAR_IP_OCTET Ljava/lang/String; ConstantValue ] REGULAR_PORTS ^ patternPrivateIP Ljava/util/regex/Pattern; <init> ()V Code LineNumberTable LocalVariableTable this GLorg/zaproxy/zap/extension/ascanrules/TestInfoPrivateAddressDisclosure; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription getCategory getSolution getReference init scan msg *Lorg/parosproxy/paros/network/HttpMessage; txtBody matcher Ljava/util/regex/Matcher; 
sbTxtFound Ljava/lang/StringBuilder; StackMapTable [ _ ` a P getRisk <clinit> 
SourceFile %TestInfoPrivateAddressDisclosure.java   ! Private IP disclosure �A private IP such as 10.x.x.x, 172.x.x.x, 192.168.x.x has been found in the HTTP response body.  This information might be helpful for further attacks targeting internal systems. �Remove the private IP address from the HTTP response body.  For comments, use JSP/ASP comment instead of HTML/JavaScript comment which can be seen by client browsers. b c _ d e f g *   h 6 i java/lang/StringBuilder a j k l * m n 
 o (   p qW(10\.(\b(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})\.){2}\b(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})|172\.\b(3[01]|2[0-9]|1[6-9])\.\b(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})\.\b(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})|192\.168\.\b(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})\.\b(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2}))(\:\b(6553[0-5]|65[0-5][0-2][0-9]|6[0-4][0-9]{4}|[0-5]?[0-9]{0,4}))? r s Eorg/zaproxy/zap/extension/ascanrules/TestInfoPrivateAddressDisclosure 3org/parosproxy/paros/core/scanner/AbstractAppPlugin $\b(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2}) ?\b(6553[0-5]|65[0-5][0-2][0-9]|6[0-4][0-9]{4}|[0-5]?[0-9]{0,4}) (org/parosproxy/paros/network/HttpMessage java/lang/String java/util/regex/Matcher 
getBaseMsg ,()Lorg/parosproxy/paros/network/HttpMessage; getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody toString java/util/regex/Pattern 3(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher; find ()Z group append -(Ljava/lang/String;)Ljava/lang/StringBuilder; length bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V compile .(Ljava/lang/String;I)Ljava/util/regex/Pattern; !                                 !  "   /     *� �    #       $ $        % &    ' (  "   ,     �    #       L $        % &    ) *  "   -     �    #       T $        % &    + ,  "   ,     �    #       ] $        % &    - *  "   -     �    #       e $        % &    . (  "   ,     �    #       n $        % &    / *  "   -     �    #       v $        % &    0 *  "   ,     �    #       � $        % &    1 !  "   +      �    #       � $        % &    2 !  "   �     Q*� L+� � M� ,� 	N� 
Y� :-� � -� � � W���� � *� +� �    #   & 	   �  �  �  �  � % � 7 � ? � P � $   4    Q % &    L 3 4   D 5    < 6 7   3 8 9  :    �   ; < = > ?    @ (  "   ,     �    #       � $        % &    A !  "   #      
� � �    #       *  B    CPK
    �K�A!~��  �  ?   org/zaproxy/zap/extension/ascanrules/TestInfoSessionIdURL.class����   3 �
 9 � � � � �
 8 �
 � �
 � �
 � �	 8 �
 3 �
 � �
 � �
 8 � �
 � �
 � �
 � � �
 8 � �
 8 � �
 � �
 � �
 � �	 8 �
 � �
 � � � � �
 8 � �
 3 �	 8 � �	 8 � �	 8 � �	 8 � �	 8 � �	 8 � �	 8 � �	 8 � � � � � � � � staticSessionIDPHP1 Ljava/util/regex/Pattern; staticSessionIDPHP2 staticSessionIDJava staticSessionIDASP staticSessionIDColdFusion staticSessionIDJW staticSessionIDWebLogic staticSessionIDApache staticSessionIDList [Ljava/util/regex/Pattern; paramHostHttp Ljava/lang/String; ConstantValue � paramHostHttps � staticLinkCheck alertReferer descReferer solutionReferer <init> ()V Code LineNumberTable LocalVariableTable this ;Lorg/zaproxy/zap/extension/ascanrules/TestInfoSessionIdURL; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription getCategory getSolution getReference init scan e ,Lorg/apache/commons/httpclient/URIException; kb i I base *Lorg/parosproxy/paros/network/HttpMessage; uri matcher Ljava/util/regex/Matcher; sessionIdValue sessionIdName StackMapTable � � � � � checkSessionIDExposure -(Lorg/parosproxy/paros/network/HttpMessage;)V host msg body risk linkHostName 
Exceptions getRisk <clinit> 
SourceFile TestInfoSessionIdURL.java O P Session ID in URL rewrite �URL rewrite is used to track user session ID.  The session ID may be disclosed in referer header.  Besides, the session ID can be stored in browser history or server logs. ~For secure content, put session ID in cookie.  To be even more secure consider to use a combination of cookie and URL rewrite. :http://seclists.org/lists/webappsec/2002/Oct-Dec/0111.html � � � � � � � � � � Y C D j � � � � � � � � sessionId/nameValue � � � � � � � �   � � sessionId/name t u *org/apache/commons/httpclient/URIException � � � � � K D � Y � � Referer expose session ID �Hyperlink to other host name is found.  As session ID URL rewrite is used, it may be disclosed in referer header to external host. �This is a risk if the session ID is sensitive and the hyperlink refer to an external host.  For secure content, put session ID in secured session cookie. � � (PHPSESSION)=\w+ � � : ; (PHPSESSID)=\w+ < ; (JSESSIONID)=\w+ = ; (ASPSESSIONID)=\w+ > ; (CFTOKEN)=\w+ ? ; (JWSESSIONID)=\w+ @ ; (WebLogicSession)=\w+ A ; (SESSIONID)=\w+ B ; java/util/regex/Pattern src\s*=\s*"?http://([\w\.\-_]+)  href\s*=\s*"?http://([\w\.\-_]+)  src\s*=\s*"?https://([\w\.\-_]+) !href\s*=\s*"?https://([\w\.\-_]+) 9org/zaproxy/zap/extension/ascanrules/TestInfoSessionIdURL 3org/parosproxy/paros/core/scanner/AbstractAppPlugin http://([\w\.\-_]+) https://([\w\.\-_]+) (org/parosproxy/paros/network/HttpMessage java/lang/String java/util/regex/Matcher 
getBaseMsg ,()Lorg/parosproxy/paros/network/HttpMessage; getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI toString 3(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher; find ()Z group (I)Ljava/lang/String; getKb (()Lorg/parosproxy/paros/core/scanner/Kb; $org/parosproxy/paros/core/scanner/Kb 	getString &(Ljava/lang/String;)Ljava/lang/String; equals (Ljava/lang/Object;)Z add '(Ljava/lang/String;Ljava/lang/Object;)V bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody 	getSecure getHost compareToIgnoreCase (Ljava/lang/String;)I �(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V compile .(Ljava/lang/String;I)Ljava/util/regex/Pattern; ! 8 9    
 : ;   
 < ;   
 = ;   
 > ;   
 ? ;   
 @ ;   
 A ;   
 B ;   
 C D    E F  G    H  I F  G    J  K D    L F  G      M F  G      N F  G        O P  Q   /     *� �    R       ( S        T U    V W  Q   ,     �    R       H S        T U    X Y  Q   -     �    R       P S        T U    Z [  Q   ,     �    R       Z S        T U    \ Y  Q   -     �    R       b S        T U    ] W  Q   ,     �    R       j S        T U    ^ Y  Q   -     �    R       r S        T U    _ Y  Q   -     �    R       z S        T U    ` P  Q   +      �    R       � S        T U    a P  Q  �  	   �*� L+� � � 	MN::6� 
�� �� 
2,� N-� � h-� :-� :*� � :� � � *� � *+� *� � :*� � *+� � :� 	���}�  � � �   R   Z    �  �  �  �  �  � $ � / � 6 � = � D � O � ^ � i � x � � � � � � � � � � � � � � � S   \ 	 �   b c  O L d F   � e f    � T U    � g h   � i F   � j k   � l F   � m F  n   - �   o p q r q q  � B q] s� �   t u  Q  L     z+� � M+� � � � >::6� �� R� 2,� :� � 8� :+� � � :� � * +� !��Ƅ����    R   :    �  �  �  �  � * � 6 � > � F � R � \ � p � s � y � S   R  R  v F  ! X e f    z T U     z w h   r x F   b y f   _ z F   \ j k  n   ( �  q@� 	  o p q q r  9�  {       | W  Q   ,     �    R       � S        T U    } P  Q   �      �"
� #� $%
� #� &'
� #� ()
� #� *+
� #� ,-
� #� ./
� #� 01
� #� 2� 3Y� $SY� &SY� (SY� ,SY� *SY� .SY� 0SY� 2S� 
� 3Y4
� #SY5
� #SY6
� #SY7
� #S� �    R   * 
   5 
 6  7  8 ( 9 2 : < ; F < P > � �  ~    PK
    �K�A� �;  ;  <   org/zaproxy/zap/extension/ascanrules/TestInjectionCRLF.class����   3 �
 1 k l
  k m
  n	 0 o
 . p
 q r
 s t
  u	 0 v w	 0 x y	 0 z {	 0 | }	 0 ~ 	 0 � �	 0 �	 0 � �	 0 � �
 � �	 0 �  �C � � � �
 0 �
 0 �
 0 �
 0 � �
 � �
 � u
 � �
 � � �
 0 � �
 . k � � staticRandomGenerator Ljava/util/Random; randomString Ljava/lang/String; cookieTamper1 cookieTamper2a cookieTamper2b cookieTamper3a cookieTamper3b cookieTamper4a cookieTamper4b 
PARAM_LIST [Ljava/lang/String; patternCookieTamper Ljava/util/regex/Pattern; <init> ()V Code LineNumberTable LocalVariableTable this 8Lorg/zaproxy/zap/extension/ascanrules/TestInjectionCRLF; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription msg getCategory getSolution getReference init scan Q(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)V e Ljava/lang/Exception; i I *Lorg/parosproxy/paros/network/HttpMessage; param value 
bingoQuery StackMapTable � � checkResult Q(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)Z attack matcher Ljava/util/regex/Matcher; � getRisk <clinit> 
SourceFile TestInjectionCRLF.java A B java/lang/StringBuilder Tamper= � � 2 3 � � � � � � � � � K 4 5 Set-cookie:  6 5 any
Set-cookie:  7 5 any?
Set-cookie:  8 5 any
Set-cookie:  9 5 any?
Set-cookie:  : 5 
 ; 5 < 5 java/lang/String = > \nSet-cookie:  � � � ? @ CRLF injection �Cookie can be set via CRLF injection.  It may also be possible to set arbitrary HTTP response header.
In addition, by carefully crafting the injected response using cross-site script, cache poisiong vulnerability may also exist. bType check the submitted parameter carefully.  Do not allow CRLF to be injected by filtering CRLF. �<ul><li>http://www.watchfire.com/resources/HTTPResponseSplitting.pdf</li><li>http://webappfirewall.com/lib/crlf-injection.txtnull</li><li>http://www.securityfocus.com/bid/9804</li></ul> � � � � � � a b java/lang/Exception � � � � d � � � �   � � java/util/Random 6org/zaproxy/zap/extension/ascanrules/TestInjectionCRLF 8org/parosproxy/paros/core/scanner/AbstractAppParamPlugin java/util/regex/Matcher append -(Ljava/lang/String;)Ljava/lang/StringBuilder; nextLong ()J java/lang/Math abs (J)J java/lang/Long toString (J)Ljava/lang/String; java/util/regex/Pattern compile .(Ljava/lang/String;I)Ljava/util/regex/Pattern; 	getNewMsg ,()Lorg/parosproxy/paros/network/HttpMessage; setParameter b(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; sendAndReceive .(Lorg/parosproxy/paros/network/HttpMessage;Z)V (org/parosproxy/paros/network/HttpMessage getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader 3(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher; find ()Z bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V ! 0 1    
 2 3    4 5    6 5    7 5    8 5    9 5    : 5    ; 5    < 5    = >    ? @     A B  C  �    A*� *� Y� � � � � � 	� � 
� *� Y� � *� � � 
� *� Y� � *� � � 
� *� Y� � *� � � 
� *� Y� � *� � � 
� *� Y� � *� � � 
� *� Y� � *� � � � 
� *� Y� � *� � � � 
� *� Y*� SY*� SY*� SY*� SY*� SY*� SY*� S� *� Y� � *� � � 

� � �    D   .    #  & & ' @ ( Z ) t * � + � , � - � 0! 2 E      A F G    H I  C   -     �    D       : E        F G    J K  C   -     �    D       B E        F G    L M  C   ,     �    D       L E        F G    N K  C   =      L+�    D   
    U  W E        F G     O 5   P I  C   ,     �    D       _ E        F G    Q K  C   -     !�    D       g E        F G    R K  C   =     "L+�    D   
    p  u E        F G     O 5   S B  C   +      �    D       ~ E        F G    T U  C   �     G:6*� �� 9*� #L*+,*� 2� $:*+� %*+,*� 2� &� �� :���ñ  $ : > '  D   .    �  �  �  � $ � * � : � ; � > � @ � F � E   H  @   V W   @ X Y    G F G     G O Z    G [ 5    G \ 5   D ] 5  ^    �  _4B `�   a b  C   �     (*� +� (� )� *:� +� *,-,+� -��    D       �  �  � $ � & � E   4    ( F G     ( O Z    ( [ 5    ( c 5    d e  ^    � & f  g I  C   ,     �    D       � E        F G    h B  C   #      � .Y� /� �    D       %  i    jPK
    �K�A׆OP�  �  >   org/zaproxy/zap/extension/ascanrules/TestParameterTamper.class����   3 �
 4 p  �H q r s t
 2 u
 2 v w	 2 x
 	 y
 z {
 | }
 ~ 	 2 �
 2 �
 | �
 � �
 2 �
 � �
 # � �
  p	 2 �
 2 �	 2 �
  �
 2 �	 2 �	 2 �	 2 �	 2 �	 2 �	 2 � � � � �
 � � � �
 � � � � � � � � � �
 z � � 
PARAM_LIST [Ljava/lang/String; patternErrorJava1 Ljava/util/regex/Pattern; patternErrorJava2 patternErrorVBScript patternErrorODBC1 patternErrorODBC2 patternErrorJet patternErrorPHP patternErrorTomcat log Lorg/apache/log4j/Logger; <init> ()V Code LineNumberTable LocalVariableTable this :Lorg/zaproxy/zap/extension/ascanrules/TestParameterTamper; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription msg Ljava/lang/String; getCategory getSolution getReference init scan Q(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)V e Ljava/lang/Exception; i I *Lorg/parosproxy/paros/network/HttpMessage; param value attack 	normalMsg StackMapTable � � � w checkResult c(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z normalHTTPResponse sb Ljava/lang/StringBuilder; � getRisk <clinit> 
SourceFile TestParameterTamper.java B C Parameter tampering �Certain parameter caused error page or Java stacktrace to be displayed.  This indicated lack of exception handling and potential areas for further exploit. �Identify the cause of the error and fix it.  Do not trust client side input and enforece tight check in the server side.  Besides, catch the exception properly.  Use a generic 500 error page for internal server error.   � � � � java/lang/Exception @ A � L � � � � � � � � J 5 6 � � � � � � L f g � � � � � java/lang/StringBuilder 7 8 � � 9 8 � � : 8 ; 8 < 8 = 8 ? 8 > 8 java/lang/String @ + %00 � � � | javax\.servlet\.\S+ � � � #invoke.+exception|exception.+invoke .Microsoft(\s+|&nbsp)*VBScript(\s+|&nbsp)+error 1Microsoft OLE DB Provider for ODBC Drivers.*error ODBC.*Drivers.*error $Microsoft JET Database Engine.*error  on line <b> F(Apache Tomcat).*(^Caused by:|HTTP Status 500 - Internal Server Error) 8org/zaproxy/zap/extension/ascanrules/TestParameterTamper � � 8org/parosproxy/paros/core/scanner/AbstractAppParamPlugin (org/parosproxy/paros/network/HttpMessage 	getNewMsg ,()Lorg/parosproxy/paros/network/HttpMessage; sendAndReceive -(Lorg/parosproxy/paros/network/HttpMessage;)V 
getMessage org/apache/log4j/Logger warn *(Ljava/lang/Object;Ljava/lang/Throwable;)V getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader getStatusCode setParameter b(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody toString +org/parosproxy/paros/network/HttpStatusCode isServerError (I)Z equals (Ljava/lang/Object;)Z matchBodyPattern _(Lorg/parosproxy/paros/network/HttpMessage;Ljava/util/regex/Pattern;Ljava/lang/StringBuilder;)Z bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 0org/parosproxy/paros/core/scanner/AbstractPlugin getURLDecode &(Ljava/lang/String;)Ljava/lang/String; java/util/regex/Pattern compile .(Ljava/lang/String;I)Ljava/util/regex/Pattern; 	getLogger ,(Ljava/lang/Class;)Lorg/apache/log4j/Logger; ! 2 4   
 
 5 6   
 7 8   
 9 8   
 : 8   
 ; 8   
 < 8   
 = 8   
 > 8   
 ? 8   
 @ A     B C  D   /     *� �    E       - F        G H    I J  D   -     �    E       I F        G H    K L  D   -     �    E       Q F        G H    M N  D   ,     �    E       [ F        G H    O L  D   =     L+�    E   
    d  e F        G H     P Q   R J  D   ,     �    E       m F        G H    S L  D   -     �    E       u F        G H    T L  D   -     �    E       ~ F        G H    U C  D   +      �    E       � F        G H    V W  D  �     �:*� :*� � :� 
� � �� �  ȟ �6� �� a*� L� *+� W:� *+,� 2� W� 2:*+� *+,� � � � �� :� 
� � �����  	   	 j � � 	  E   b    �  � 	 �  �  �  � ! � " � 0 � 1 � = � B � G � O � U � b � j � o � � � � � � � � � � � � � F   \ 	   X Y  �  X Y  4 g Z [    � G H     � P \    � ] Q    � ^ Q   � _ Q  	 � ` \  a   / 
�   b c d d d c  e�  B e�   f g  D  ^     �+� �  ȟ +� � � � �+� � � � �� Y� :*+� � �  *+� � � *,-� +� �*+� � � D*+� � � 7*+� � � **+�  � � *+� !� � *+� "� � *,-� +� ��    E   2    �  �  � + � - � 6 � O � ^ � ` � � � � � � � F   >    � G H     � P \    � ] Q    � _ Q    � h Q  6 � i j  a    � 2 k� M  l J  D   ,     �    E       � F        G H    m C  D   �      �� #YSYSY$SY%SY&� 'SY(S� )
� *� +
� *� ,
� *� -
� *� .
� *� /
� *�  0
� *� "1
� *� ! 2� 3� 
�    E   * 
   3 ) 6 3 7 = 9 G : Q ; [ < e = o > y A  n    oPK
    �K�AB��_K.  K.  <   org/zaproxy/zap/extension/ascanrules/TestPathTraversal.class����   3�
 � �	 � �
 � � �
 � � �
 � � � �
 	 �
 � � � � � � � � �
 	 �
 	 �
 	 �
 	 	 �

 �
 	
		
							 �	 �
 ! �
 �

 !

 ! �



 (
 ( !	 �"
 �#$%	&'
&(
 !)
 (*
 !+
,	&-
./012
 ?3
 �4
5
6 
7
8 	 �9
:;
:<
=>
6?@A
BCDEF
 �GHIJK
L M	 �NO	 �PQR
 ?S
TUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~��������
���
�� LOCAL_FILE_TARGET_PREFIXES [Ljava/lang/String; LOCAL_FILE_TARGETS LOCAL_FILE_PATTERNS REMOTE_FILE_TARGET_PREFIXES REMOTE_FILE_TARGETS REMOTE_FILE_PATTERNS vuln %Lorg/zaproxy/zap/model/Vulnerability; log Lorg/apache/log4j/Logger; <init> ()V Code LineNumberTable LocalVariableTable this 8Lorg/zaproxy/zap/extension/ascanrules/TestPathTraversal; getId ()I getName ()Ljava/lang/String; StackMapTable getDependency ()[Ljava/lang/String; getDescription getCategory getSolution getReference ref Ljava/lang/String; i$ Ljava/util/Iterator; sb Ljava/lang/StringBuilder; �� � init scan params Ljava/util/TreeSet; target response i I prefix h prefixedUrlfilename urlfilename matcher Ljava/util/regex/Matcher; msg *Lorg/parosproxy/paros/network/HttpMessage; currentHtmlParameter ,Lorg/parosproxy/paros/network/HtmlParameter; exceptionPattern Ljava/util/regex/Pattern; exceptionMatcher errorPattern errorMatcher iter prefixCountLFI prefixCountRFI prefixCountOurUrl 
htmlParams e Ljava/lang/Exception; LocalVariableTypeTable ALjava/util/TreeSet<Lorg/parosproxy/paros/network/HtmlParameter;>; BLjava/util/Iterator<Lorg/parosproxy/paros/network/HtmlParameter;>;����/ getRisk <clinit> 
SourceFile TestPathTraversal.java � � � ��� � Path traversal � � 2Failed to load vulnerability description from file � � /Failed to load vulnerability solution from file java/lang/StringBuilder���������� java/lang/String� ������ � 0Failed to load vulnerability reference from file � ���� Attacking at Attack Strength: ��������������� � � � � java/util/TreeSet��������� *org/parosproxy/paros/network/HtmlParameter 
Checking [���� � ] [�� ], [�� ] parameter [ � � #] for Path Traversal to local files � ��� .] for Path Traversal (local file) with value [ ]������� ���������� java/lang/Exception Unsupported parameter type [ ] for param [ ] on [ ���������� � ���� ������ �  Path Traversal (local file) on [ ] with value [��   [ ]   �� &thishouldnotexistandhopefullyitwillnot 	Exception Error NIt IS possible to check for local file Path Traversal on the url filename on [� $] for Path Traversal to remote files � � -] for remote file Path Traversal with value [ � � !Path Traversal (remote file) on [ .Error scanning parameters for Path Traversal: � ��� / \ /../../ /../../../../../../../../../../../../../../../.. 4/../../../../../../../../../../../../../../../../../ \..\..\ /..\..\..\..\..\..\..\..\..\..\..\..\..\..\..\.. 4\..\..\..\..\..\..\..\..\..\..\..\..\..\..\..\..\..\ ./ ../ ../../ /.. /../ /../.. /./ .\ ..\ ..\..\ \.. \..\ \..\.. \.\ file:// fiLe:// file: fiLe: FILE: FILE:// 
etc/passwd 
etc\passwd Windows/system.ini Windows\system.ini WEB-INF/web.xml WEB-INF\web.xml 
root:.:0:0 \[drivers\] 
</web-app> http:// HTTP:// https:// HTTPS:// HtTp:// HtTpS:// www.google.com/ www.google.com:80/ www.google.com #www.google.com/search?q=OWASP%20ZAP &www.google.com:80/search?q=OWASP%20ZAP I'm Feeling Lucky OWASP ZAP - Google Search wasc_33��� 6org/zaproxy/zap/extension/ascanrules/TestPathTraversal�� 3org/parosproxy/paros/core/scanner/AbstractAppPlugin java/util/Iterator java/util/regex/Matcher (org/parosproxy/paros/network/HttpMessage java/util/regex/Pattern #org/zaproxy/zap/model/Vulnerability getAlert getReferences ()Ljava/util/List; java/util/List iterator ()Ljava/util/Iterator; hasNext ()Z next ()Ljava/lang/Object; length append (C)Ljava/lang/StringBuilder; -(Ljava/lang/String;)Ljava/lang/StringBuilder; toString org/apache/log4j/Logger isDebugEnabled getAttackStrength AttackStrength InnerClasses ;()Lorg/parosproxy/paros/core/scanner/Plugin$AttackStrength; -(Ljava/lang/Object;)Ljava/lang/StringBuilder; debug (Ljava/lang/Object;)V� 7org/parosproxy/paros/core/scanner/Plugin$AttackStrength LOW 9Lorg/parosproxy/paros/core/scanner/Plugin$AttackStrength; MEDIUM HIGH INSANE 
getBaseMsg ,()Lorg/parosproxy/paros/network/HttpMessage; getUrlParams ()Ljava/util/TreeSet; addAll (Ljava/util/Collection;)Z getFormParams getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader 	getMethod getURI %()Lorg/apache/commons/httpclient/URI; getType Type 3()Lorg/parosproxy/paros/network/HtmlParameter$Type; 	getNewMsg /org/parosproxy/paros/network/HtmlParameter$Type url 1Lorg/parosproxy/paros/network/HtmlParameter$Type; equals (Ljava/lang/Object;)Z remove X(Lorg/parosproxy/paros/network/HtmlParameter$Type;Ljava/lang/String;Ljava/lang/String;)V add setGetParams (Ljava/util/TreeSet;)V form setFormParams (Ljava/lang/String;)V sendAndReceive -(Lorg/parosproxy/paros/network/HttpMessage;)V getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody compile -(Ljava/lang/String;)Ljava/util/regex/Pattern; 3(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher; find getStatusCode info bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V !org/apache/commons/httpclient/URI 
getMessage error %org/zaproxy/zap/model/Vulnerabilities getVulnerability 9(Ljava/lang/String;)Lorg/zaproxy/zap/model/Vulnerability; 	getLogger ,(Ljava/lang/Class;)Lorg/apache/log4j/Logger; (org/parosproxy/paros/core/scanner/Plugin ! � �     � �    � �    � �    � �    � �    � �   
 � �   
 � �     � �  �   /     *� �    �       ( �        � �    � �  �   -     �    �       � �        � �    � �  �   K     � � 
� � ��    �       �  �  � �        � �   �      � �  �   ,     �    �       � �        � �    � �  �   K     � � 
� � ��    �       �  �  � �        � �   �      � �  �   ,     �    �       � �        � �    � �  �   K     � � 
� � ��    �       �  �  � �        � �   �      � �  �   �     L� � F� 	Y� 
L� � �  M,�  � $,�  � N+� � 
+
� W+-� W���+� ��    �   & 	   �  �  � - � 4 � ; � A � D � I � �   *  -  � �   * � �   ; � �    L � �   �    �  � ��   �� �   � �  �   +      �    �       � �        � �    � �  �  ]    
k<=>� � � � � 	Y� 
� *� � � � *� � � <=>� B*� � � <=>� /*� � � <=>� *� � � � �<�  �=� �>� !Y� ":*� #� $� %W*� #� &� %W� ':�  �	�::�  � (:� � � V� � 	Y� 
)� *� #� *� +� ,� *� #� *� -� .� � /� 0� � 1� 2� � � 6		��� 	2:
6� 3���� 32:*� 4:� � � a� � 	Y� 
)� � *� +� ,� � *� -� .� � /� 0� � 1� 5� 
� � 6� � � � /� 7� 8� G� $:� 9W� (Y� /� 1� 	Y� 

� � � � :� ;W� <� �� /� =� 8� G� &:� 9W� (Y� /� 1� 	Y� 

� � � � :� ;W� >� T� ?Y� 	Y� 
@� � /� A� � 1� B� � *� +� ,� � *� -� 6� � � C�*� D� 	Y� 
� E� F� � G� H� � :� I2� J� K:� L� �� E� M Ƞ ɲ � 	Y� 
N� � *� +� ,� � *� -� .� � /� 0� � 1� O� 
� � 6� � � P*Q� 	Y� 
R� � /� S� � 1� � � 	Y� 
� *� +� T� � *� -� � � 	Y� 

� � � � U����v�	��_*� 4:� /� 7� 8� 5� $:		� 9W	� (Y� /� 1V� :� ;W	� <� �� /� =� 8� 5� &:		� 9W	� (Y� /� 1V� :� ;W	� >� T� ?Y� 	Y� 
@� � /� A� � 1� B� � *� +� ,� � *� -� 6� � � C�*� DW� J:		� G� H� K:
X� J:� G� H� K:� E� M Ƞ 
� L� � L�Y� � � R� � 	Y� 
Y� � *� +� ,� � *� -� .� � /� 0� � 1� 6� � � � *� -� Z:6�� 	Y� 
� 2� � � :*� 4:� /� 7� 8� 5� $:� 9W� (Y� /� 1� :� ;W� <� �� /� =� 8� 5� &:� 9W� (Y� /� 1� :� ;W� >� T� ?Y� 	Y� 
@� � /� A� � 1� B� � *� +� ,� � *� -� 6� � � C�*� D	� G� H� K:
� G� H� K:� E� M Ƞ �
� L� �� L� �� � 	Y� 
N� � *� +� ,� � *� -� .� � /� 0� � 1� O� � 6� � � P*Q� 	Y� 
R� � /� S� � 1� � � 	Y� 
� *� +� T� � *� -� � � U����� � � V� � 	Y� 
)� *� #� *� +� ,� *� #� *� -� .� � /� 0� � 1� [� � � 6���  2:6� \���� \2:*� 4:� � � a� � 	Y� 
)� � *� +� ,� � *� -� .� � /� 0� � 1� ]� � � 6� � � � /� 7� 8� G� $:� 9W� (Y� /� 1� 	Y� 
� � � � :� ;W� <� �� /� =� 8� G� &:� 9W� (Y� /� 1� 	Y� 
� � � � :� ;W� >� T� ?Y� 	Y� 
@� � /� A� � 1� B� � *� +� ,� � *� -� 6� � � C�*� D� 	Y� 
� E� F� � G� H� � :� ^2� J� K:� L� �� E� M Ƞ ɲ � 	Y� 
_� � *� +� ,� � *� -� .� � /� 0� � 1� O� � � 6� � � P*Q� 	Y� 
R� � /� S� � 1� � � 	Y� 
� *� +� T� � *� -� � � 	Y� 
� � � � U����v���_��a� !L� � 	Y� 
`� +� a� � � b��   �
L ?�<
L ?=
9
L ?
:
I
L ?  �   �   �  �  �  � + � 5 � 7 � 9 � > � H � J � L � Q � [ � ] � _ � d � n � s � x � } � � � � �	 �
 �(0<DJ��� �!�" #%& 'K(R)U*�-�/�0�2�3R4�5���>�?�A�B�CD	EG!H)IBJIKLL�O�R�S�T�U�W�ZE[R^[_u`{b�d�e�f�g�h�j�k�l�m�n�oLrRuavpx�~�<�=^C�������������1�?�F�N�y���������������	&�	,�	N�	^�	t�	��
9�
:�
@�
F�
I�
L�
M�
i�
j� �  ` #� : � �  : � � Dv � � � � � � 3� � � 0� � � 
"� � � 	� ( � � 	! ( � � 	� ( � � � ( � � u� � � U� � � R� � � F : � � � : � � �v � � 	N � � � �� � � �� � � �� � �  �	� � �  �	� � �  �	� � � �� � � 	�� � � 
�� � � �w � �  �	� � �  
G � �  
E � �  
C � �  �	� � � 
M  � �   
k � �   �   f 
� : � �  : � � � ( � � 	! ( � � 	� ( � � � ( � � F : � � � : � �  �	� � �  �	� � �  �   � (� +� ) � �� w � � �� �  �� } �� Q� Q� P�� � � E?� P� O  � � � � � � � � � �  � W�  �� e �?� P� �� � [� �  �� } �� Q� Q� P�� �   � � �  �   �  B �  � �  �   ,     �    �      � �        � �    � �  �  �     �� YcSYdSYeSYfSYQSYgSYhSYiSYjSY	kSY
lSYmSYnSYoSYpSYqSYdSYrSYsSYtSYuSYvSYwSYxSYySYzSY{SY|SY}SY~S� � YSY�SY�SY�SY�SY�S� 3� Y�SY�SY�SY�SY�SY�S� I� Y�SYQSY�SY�SY�SY�SY�S�  � Y�SY�SY�SY�SY�S� \� Y�SY�SY�SY�SY�S� ^�� ��  �� �� �    �   "    - � Q � ] m. tN n �v �  �    ��    	��@& (�@PK
    �K�AV����  �  7   org/zaproxy/zap/extension/ascanrules/TestRedirect.class����   3 �
 + ]	 ) ^
 _ ` a
 _ b c
 _ d e f
 	 ]
 _ g h i j k j l m
 	 n
 	 o
 	 p
 	 q r s
 t u
 v w
 x q
  y z
 ) {
 ) |
 t }
 ~ 
 � � �
 ~ �
 ) � �	 ) �
 # �
 � � �
 � � �
 � � � REDIR1 Ljava/lang/String; ConstantValue REDIR2 vuln %Lorg/zaproxy/zap/model/Vulnerability; log Lorg/apache/log4j/Logger; <init> ()V Code LineNumberTable LocalVariableTable this 3Lorg/zaproxy/zap/extension/ascanrules/TestRedirect; getId ()I getName ()Ljava/lang/String; StackMapTable getDependency ()[Ljava/lang/String; getDescription getCategory getSolution getReference ref i$ Ljava/util/Iterator; sb Ljava/lang/StringBuilder; f � m init scan Q(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)V redirect e Ljava/lang/Exception; msg *Lorg/parosproxy/paros/network/HttpMessage; param value � getRisk <clinit> 
SourceFile TestRedirect.java 4 5 0 1 � � > URL Redirector Abuse B > 2Failed to load vulnerability description from file D > /Failed to load vulnerability solution from file java/lang/StringBuilder � � � � � � � � � � java/lang/String � < � � � � � > 0Failed to load vulnerability reference from file http://www.owasp.org � � � � � � � � � http://www.google.com � � � � � � � � < � � � Location � � � � java/lang/Exception 2 3 � > � � � wasc_38 � � � 1org/zaproxy/zap/extension/ascanrules/TestRedirect � � 8org/parosproxy/paros/core/scanner/AbstractAppParamPlugin java/util/Iterator #org/zaproxy/zap/model/Vulnerability getAlert getReferences ()Ljava/util/List; java/util/List iterator ()Ljava/util/Iterator; hasNext ()Z next ()Ljava/lang/Object; length append (C)Ljava/lang/StringBuilder; -(Ljava/lang/String;)Ljava/lang/StringBuilder; toString (org/parosproxy/paros/network/HttpMessage getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI 
startsWith (Ljava/lang/String;)Z setParameter b(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; sendAndReceive /(Lorg/parosproxy/paros/network/HttpMessage;ZZ)V getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader getStatusCode +org/parosproxy/paros/network/HttpStatusCode isRedirection (I)Z 	getHeader &(Ljava/lang/String;)Ljava/lang/String; bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 
getMessage org/apache/log4j/Logger error *(Ljava/lang/Object;Ljava/lang/Throwable;)V %org/zaproxy/zap/model/Vulnerabilities getVulnerability 9(Ljava/lang/String;)Lorg/zaproxy/zap/model/Vulnerability; 	getLogger ,(Ljava/lang/Class;)Lorg/apache/log4j/Logger; ! ) +     , -  .      / -  .     
 0 1   
 2 3     4 5  6   /     *� �    7        8        9 :    ; <  6   .     N*�    7       * 8        9 :    = >  6   K     � � 
� � ��    7       /  0  2 8        9 :   ?      @ A  6   ,     �    7       7 8        9 :    B >  6   K     � � 
� � ��    7       <  =  ? 8        9 :   ?      C <  6   ,     �    7       D 8        9 :    D >  6   K     � � 
� � ��    7       I  J  L 8        9 :   ?      E >  6   �     L� � F� 	Y� 
L� � �  M,�  � $,�  � N+� � 
+
� W+-� W���+� ��    7   & 	   Q  R  S - T 4 U ; W A X D Y I [ 8   *  -  F -   * G H   ; I J    L 9 :   ?    �  K L�   M� �   N 5  6   +      �    7       a 8        9 :    O P  6       h:+� � � � � :*+,� W*+� +� � � � !+�  � !� � *,+� "�� :� $� %� &�    T X #  7   6    h  i  k  n # o * q 7 s H u T w U | X z Z { g ~ 8   >   Q Q -  Z  R S    h 9 :     h T U    h V -    h W -  ?    �  M9�  B X  Y <  6   ,     �    7       � 8        9 :    Z 5  6   .      '� (�  )� *� $�    7   
    $  &  [    \PK
    �K�Aa�:�3  3  @   org/zaproxy/zap/extension/ascanrules/TestServerSideInclude.class����   3 e
  A  �I B C D E F
  G
  H	  I
  J
  K L
  M N O	  P Q R
 S T U V W SSI_UNIX Ljava/lang/String; ConstantValue 	SSI_UNIX2 SSI_WIN SSI_WIN2 patternSSIUnix Ljava/util/regex/Pattern; patternSSIWin <init> ()V Code LineNumberTable LocalVariableTable this <Lorg/zaproxy/zap/extension/ascanrules/TestServerSideInclude; getId ()I getName ()Ljava/lang/String; getDependency ()[Ljava/lang/String; getDescription msg getCategory getSolution getReference init scan Q(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)V e Ljava/lang/Exception; *Lorg/parosproxy/paros/network/HttpMessage; param value StackMapTable L getRisk <clinit> 
SourceFile TestServerSideInclude.java ! " Server side include �Certain parameters may cause Server Side Include commands to be executed.  This may allow database connection or arbitrary code to be executed.~Do not trust client side input and enforece tight check in the server side.  Disable server side include.
. Refer to manual to disable Sever Side Include.
. Use least privilege to run your web server or application server.
For Apache, disable the following:
Options Indexes FollowSymLinks Includes
AddType application/x-httpd-cgi .cgi
AddType text/x-server-parsed-html .html
 ,http://www.carleton.ca/~dmcfet/html/ssi.html <!--#EXEC cmd="ls /"--> X Y Z [   \ ] ^ _ java/lang/Exception ` a "><!--#EXEC cmd="ls /"-->< <!--#EXEC cmd="dir \"-->    "><!--#EXEC cmd="dir \"-->< \broot\b.*\busr\b b c d &\bprogram files\b.*\b(WINDOWS|WINNT)\b :org/zaproxy/zap/extension/ascanrules/TestServerSideInclude 8org/parosproxy/paros/core/scanner/AbstractAppParamPlugin setParameter b(Lorg/parosproxy/paros/network/HttpMessage;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; sendAndReceive -(Lorg/parosproxy/paros/network/HttpMessage;)V matchBodyPattern _(Lorg/parosproxy/paros/network/HttpMessage;Ljava/util/regex/Pattern;Ljava/lang/StringBuilder;)Z bingo w(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 	getNewMsg ,()Lorg/parosproxy/paros/network/HttpMessage; java/util/regex/Pattern compile .(Ljava/lang/String;I)Ljava/util/regex/Pattern; !                                          
     
        ! "  #   /     *� �    $       " %        & '    ( )  #   -     �    $       0 %        & '    * +  #   -     �    $       5 %        & '    , -  #   ,     �    $       ; %        & '    . +  #   =     L+�    $   
    @  A %        & '     /    0 )  #   ,     �    $       F %        & '    1 +  #   =     L+�    $   
    K  R %        & '     /    2 +  #   =     L+�    $   
    W  X %        & '     /    3 "  #   +      �    $       ^ %        & '    4 5  #  �     �*+,� W*+� 	*+� 
� � *,+� �� :*� L*+,� W*+� 	*+� 
� � *,+� �� :*� L*+,� W*+� 	*+� � � *,+� �� :*� L*+,� W*+� 	*+� � � *,+� �� :�    & *  , W [  ] � �  � � �   $   �     h 	 i  k  l & m ' q * p , t 1 u : v ? x K y W z X ~ [ } ] � b � k � p � | � � � � � � � � � � � � � � � � � � � � � � � � � %   R  ,   6 7  ]   6 7  �   6 7  �   6 7    � & '     � / 8    � 9     � :   ;    'B <+B <+B <+B <  = )  #   ,     �    $       � %        & '    > "  #   1      
� � 

� � �    $   
    + 
 ,  ?    @PK
    �K�A            	         �A    META-INF/��  PK
    �K�A�?�~g   g              ��+   META-INF/MANIFEST.MFPK
    �K�A                      �A�   org/PK
    �K�A                      �A�   org/zaproxy/PK
    �K�A                      �A  org/zaproxy/zap/PK
    �K�A                      �A>  org/zaproxy/zap/extension/PK
    �K�A            %          �Av  org/zaproxy/zap/extension/ascanrules/PK
    �K�Au$�  �  A           ���  org/zaproxy/zap/extension/ascanrules/TestClientBrowserCache.classPK
    �K�A^��l
#  
#  @           ���  org/zaproxy/zap/extension/ascanrules/TestCrossSiteScriptV2.classPK
    �K�Ar���]  ]  @           ��4  org/zaproxy/zap/extension/ascanrules/TestDirectoryBrowsing.classPK
    �K�A̸!ȱ  �  ?           ���E  org/zaproxy/zap/extension/ascanrules/TestExternalRedirect.classPK
    �K�A�i}�D  D  K           ���T  org/zaproxy/zap/extension/ascanrules/TestInfoPrivateAddressDisclosure.classPK
    �K�A!~��  �  ?           ��yb  org/zaproxy/zap/extension/ascanrules/TestInfoSessionIdURL.classPK
    �K�A� �;  ;  <           ���y  org/zaproxy/zap/extension/ascanrules/TestInjectionCRLF.classPK
    �K�A׆OP�  �  >           ��I�  org/zaproxy/zap/extension/ascanrules/TestParameterTamper.classPK
    �K�AB��_K.  K.  <           ��]�  org/zaproxy/zap/extension/ascanrules/TestPathTraversal.classPK
    �K�AV����  �  7           ���  org/zaproxy/zap/extension/ascanrules/TestRedirect.classPK
    �K�Aa�:�3  3  @           ����  org/zaproxy/zap/extension/ascanrules/TestServerSideInclude.classPK      s  ��    