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
    �K�A            %   org/zaproxy/zap/extension/pscanrules/PK
    �K�A{9     >   org/zaproxy/zap/extension/pscanrules/CacheControlScanner.class����   3 �
 ' U	 & V
 W X
 Y Z
 Y [
 \ ]
  ^ _
  `
 W a
 b c
 W d
 e f g
 b h
 i j k l k m n o
  p q r s
 & t u v
 & w
 & x
  y z { | }
  ~
  � � � � parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; <init> ()V Code LineNumberTable LocalVariableTable this :Lorg/zaproxy/zap/extension/pscanrules/CacheControlScanner; 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V cacheControlDirective Ljava/lang/String; i$ Ljava/util/Iterator; pragmaDirective cacheControl Ljava/util/Vector; pragma source Lnet/htmlparser/jericho/Source; LocalVariableTypeTable &Ljava/util/Vector<Ljava/lang/String;>; StackMapTable � � n 
raiseAlert @(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;)V alert )Lorg/parosproxy/paros/core/scanner/Alert; getId ()I getName ()Ljava/lang/String; 
SourceFile CacheControlScanner.java * + ( ) � � � � � � � � � � R � R .css � � � � � � � � � � � P Cache-control � � � � � � � � � � java/lang/String no-store � � no-cache must-revalidate private K L Pragma 'org/parosproxy/paros/core/scanner/Alert O P Q R * � tThe cache-control and pragma HTTPHeader have not been set properly allowing the browser and proxies to cache content   �Whenever possible ensure the cache-control HTTPHeader is set with no-cache, no-store, must-revalidate, private, and the pragma HTTPHeader is set with no-cache. Rhttps://www.owasp.org/index.php/Session_Management_Cheat_Sheet#Web_Content_Caching � � � K � 8Incomplete or no cache-control and pragma HTTPHeader set 8org/zaproxy/zap/extension/pscanrules/CacheControlScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/Vector java/util/Iterator (org/parosproxy/paros/network/HttpMessage getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader 	getSecure ()Z getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI toString toLowerCase endsWith (Ljava/lang/String;)Z getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader isImage getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody length 
getHeaders &(Ljava/lang/String;)Ljava/util/Vector; iterator ()Ljava/util/Iterator; hasNext next ()Ljava/lang/Object; indexOf (Ljava/lang/String;)I (IIILjava/lang/String;)V 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V ! & '     ( )     * +  ,   8     
*� *� �    -   
        .       
 / 0    1 2  ,   >     *+� �    -   
    $  & .        / 0      ( )   3 4  ,   ?      �    -       + .         / 0      5 6     7 8   9 :  ,  �     �+� � � �+� � � � � 	� �+� 
� � �+� � � �+� 
� :� _� :�  � N�  � :� � � *� � � � � � � � � *+� ���+� 
� :� 8� :�  � '�  � :� � � *+� ��ձ    -   :    / 3 0 > 1 C 2 ` 3 � 4 � 6 � 9 � : � ; � < � = � ? � C .   f 
 ` < ; <  J U = >  �  ? <  � . = >  > � @ A  � : B A    � / 0     � 5 6    � 7 8    � C D  E     > � @ F  � : B F  G   $ � J H I� I J� � �  H I*�   K L  ,   � 	    9� Y*� *� � :+� � � -  !"+� #*� � $�    -       F  H . Q 8 R .   4    9 / 0     9 5 6    9 7 8    9 @ <   & M N   O P  ,   .     '�    -       U .        / 0    Q R  ,   -     %�    -       Z .        / 0    S    TPK
    �K�AX��  �  D   org/zaproxy/zap/extension/pscanrules/ContentTypeMissingScanner.class����   3 �
  K	  L
 M N
 O P
 M Q R
 S T
 U V W X W Y Z
  [
  \ ] ^ _ `
  a
  b
  c
 M d
 e f
 g h i
  j
 k l m n parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; <init> ()V Code LineNumberTable LocalVariableTable this @Lorg/zaproxy/zap/extension/pscanrules/ContentTypeMissingScanner; scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V contentTypeDirective Ljava/lang/String; i$ Ljava/util/Iterator; contentType Ljava/util/Vector; source Lnet/htmlparser/jericho/Source; LocalVariableTypeTable &Ljava/util/Vector<Ljava/lang/String;>; StackMapTable o p 
raiseAlert A(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;Z)V isContentTypeMissing Z issue alert )Lorg/parosproxy/paros/core/scanner/Alert; Z 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V getName ()Ljava/lang/String; getId ()I 
SourceFile ContentTypeMissingScanner.java      q r s t u H v w Content-Type x y z o { | p } ~  � java/lang/String � ~ ; <   Content-Type header empty Content-Type header missing 'org/parosproxy/paros/core/scanner/Alert G H E F  � � � � � � � � F kEnsure each page is setting the specific and appropriate content-type value for the content being delivered � � � ; � >org/zaproxy/zap/extension/pscanrules/ContentTypeMissingScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/Vector java/util/Iterator (org/parosproxy/paros/network/HttpMessage getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody length getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader 
getHeaders &(Ljava/lang/String;)Ljava/util/Vector; iterator ()Ljava/util/Iterator; hasNext ()Z next ()Ljava/lang/Object; isEmpty (IIILjava/lang/String;)V getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI toString 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V !                 !   8     
*� *� �    "   
       #       
 $ %    & '  !   ?      �    "       $ #         $ %      ( )     * +   , -  !  	     X+� � � P+� � :� 7� :� 	 � #� 
 � :� � *+� ��٧ *+� �    "   & 	   ( 
 )  *  + 7 , ? - H / N 1 W 4 #   H  7  . /  ! * 0 1   B 2 3    X $ %     X ( )    X * +    X 4 5  6      B 2 7  8    � ! 9 :&� �   ; <  !   � 	    F:� :� Y*� *� � :+� � � -+� *� � �    "       7  8 	 9  <   > ; H E I #   H    F $ %     F ( )    F * +    F 2 /    F = >   B ? /    & @ A  8    �  B  C D  !   >     *+� �    "   
    N  O #        $ %          E F  !   -     �    "       S #        $ %    G H  !   .     '#�    "       W #        $ %    I    JPK
    �K�A=��Ă  �  @   org/zaproxy/zap/extension/pscanrules/CookieHttpOnlyScanner.class����   3 �
  J	  K
 L M N
 O P
 Q R S T S U V
 	 W X
 	 Y
  Z [ \
  ] ^
  _ `
 L a
 b c
 d e f g h
  i
 j k l m n parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; <init> ()V Code LineNumberTable LocalVariableTable this <Lorg/zaproxy/zap/extension/pscanrules/CookieHttpOnlyScanner; 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V cookie Ljava/lang/String; i$ Ljava/util/Iterator; source Lnet/htmlparser/jericho/Source; cookies1 Ljava/util/Vector; cookies2 LocalVariableTypeTable &Ljava/util/Vector<Ljava/lang/String;>; StackMapTable o p 
raiseAlert @(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;)V alert )Lorg/parosproxy/paros/core/scanner/Alert; getId ()I getName ()Ljava/lang/String; 
SourceFile CookieHttpOnlyScanner.java ! "    q r s 
Set-Cookie t u v o w x p y z { | java/lang/String } G httponly ~  @ A Set-Cookie2 'org/parosproxy/paros/core/scanner/Alert D E  Cookie set without HttpOnly flag ! �(A cookie has been set without the HttpOnly flag, which means that the cookie can be accessed by JavaScript. If a malicious script can be run on this page then the cookie will be accessible and can be transmitted to another site. If this is a session cookie then session hijacking may be possible. � � � � � � � G   5Ensure that the HttpOnly flag is set for all cookies.  www.owasp.org/index.php/HttpOnly � � � @ � Cookie no http-only flag :org/zaproxy/zap/extension/pscanrules/CookieHttpOnlyScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/Vector java/util/Iterator (org/parosproxy/paros/network/HttpMessage getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader 
getHeaders &(Ljava/lang/String;)Ljava/util/Vector; iterator ()Ljava/util/Iterator; hasNext ()Z next ()Ljava/lang/Object; toLowerCase indexOf (Ljava/lang/String;)I (IIILjava/lang/String;)V getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI toString 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V !              ! "  #   8     
*� *� �    $   
      ! %       
 & '    ( )  #   >     *+� �    $   
    &  ' %        & '           * +  #   ?      �    $       , %         & '      , -     . /   0 1  #  }     �+� � :� 8� :�  � '�  � 	:� 
� � *+� ���+� � :� 8� :�  � '�  � 	:� 
� � *+� ��ձ    $   6    0  2  3 - 4 : 5 B 7 E : P < U = r >  ? � A � C %   f 
 -  2 3   . 4 5  r  2 3  \ . 4 5    � & '     � , -    � . /    � 6 7   � 8 9  P ; : 9  ;      � 8 <  P ; : <  =    �  > ?*� �  > ?*�   @ A  #   � 	    7� Y*� � :+� � � -+� *� � �    $       F  H , R 6 T %   4    7 & '     7 , -    7 . /    7 2 3   & B C   D E  #   .     '�    $       W %        & '    F G  #   -     �    $       \ %        & '    H    IPK
    �K�An���  �  B   org/zaproxy/zap/extension/pscanrules/CookieSecureFlagScanner.class����   3 �
  K	  L
 M N
 O P
 M Q R
 S T
 U V W X W Y Z
  [ \
  ]
  ^ _ `
  a b
  c d
 O e
 f g h i j
  k
 l m n o p parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; <init> ()V Code LineNumberTable LocalVariableTable this >Lorg/zaproxy/zap/extension/pscanrules/CookieSecureFlagScanner; 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V cookie Ljava/lang/String; i$ Ljava/util/Iterator; source Lnet/htmlparser/jericho/Source; cookies1 Ljava/util/Vector; cookies2 LocalVariableTypeTable &Ljava/util/Vector<Ljava/lang/String;>; StackMapTable q r 
raiseAlert @(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;)V alert )Lorg/parosproxy/paros/core/scanner/Alert; getId ()I getName ()Ljava/lang/String; 
SourceFile CookieSecureFlagScanner.java " #   ! s t u v w x y z 
Set-Cookie { | } q ~  r � x � � java/lang/String � H secure � � A B Set-Cookie2 'org/parosproxy/paros/core/scanner/Alert E F Cookie set without secure flag " � wA cookie has been set without the secure flag, which means that the cookie can be accessed via unencrypted connections. � � � � H   �Whenever a cookie contains sensitive information or is a session token, then it should always be passed using an encrypted tunnel. Ensure that the secure flag is set for cookies containing such sensitive information. Lhttp://www.owasp.org/index.php/Testing_for_cookies_attributes_(OWASP-SM-002) � � � A � Cookie without secure flag <org/zaproxy/zap/extension/pscanrules/CookieSecureFlagScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/Vector java/util/Iterator (org/parosproxy/paros/network/HttpMessage getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader 	getSecure ()Z getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader 
getHeaders &(Ljava/lang/String;)Ljava/util/Vector; iterator ()Ljava/util/Iterator; hasNext next ()Ljava/lang/Object; toLowerCase indexOf (Ljava/lang/String;)I (IIILjava/lang/String;)V getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI toString 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V !         !     " #  $   8     
*� *� �    %   
      ! &       
 ' (    ) *  $   >     *+� �    %   
    &  ' &        ' (        !   + ,  $   ?      �    %       , &         ' (      - .     / 0   1 2  $  �     �+� � � �+� � :� 8� :� 	 � '� 
 � :� � � *+� ���+� � :� 8� :� 	 � '� 
 � :� � � *+� ��ձ    %   >    0 
 2  5  7  8 8 9 E : M < P ? [ A ` B } C � D � F � H &   f 
 8  3 4  " . 5 6  }  3 4  g . 5 6    � ' (     � - .    � / 0    � 7 8   � 9 :  [ ; ; :  <      � 9 =  [ ; ; =  >    �  ? @*� �  ? @*�   A B  $   � 	    7� Y*� � :+� � � -+� *� � �    %       K  M , V 6 X &   4    7 ' (     7 - .    7 / 0    7 3 4   & C D   E F  $   .     '�    %       [ &        ' (    G H  $   -     �    %       ` &        ' (    I    JPK
    �K�A�o�R`  `  L   org/zaproxy/zap/extension/pscanrules/CrossDomainScriptInclusionScanner.class����   3 �
 4 q	 2 r
 s t
 u v
 s w
 x y z
 { | } ~  �  � � H
  �
 s �
 � �
 2 �
 2 � �
 2 �
 2 �
  � �
 � �
 $ � � �
  �
 � � � �
 � � � � � �
 $ �
 $ �
 � �
 � � �	 2 � �
 + q �
 + �
 ) �
 + �
 � � �
 � � � parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; logger Lorg/apache/log4j/Logger; <init> ()V Code LineNumberTable LocalVariableTable this HLorg/zaproxy/zap/extension/pscanrules/CrossDomainScriptInclusionScanner; scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V src Ljava/lang/String; sourceElement  Lnet/htmlparser/jericho/Element; i$ Ljava/util/Iterator; sourceElements Ljava/util/List; source Lnet/htmlparser/jericho/Source; LocalVariableTypeTable 2Ljava/util/List<Lnet/htmlparser/jericho/Element;>; StackMapTable � � 
raiseAlert @(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;)V crossDomainScript alert )Lorg/parosproxy/paros/core/scanner/Alert; 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V getId ()I getName ()Ljava/lang/String; isScriptFromOtherDomain '(Ljava/lang/String;Ljava/lang/String;)Z 	scriptURI #Lorg/apache/commons/httpclient/URI; 
scriptHost e ,Lorg/apache/commons/httpclient/URIException; host 	scriptURL result Z � <clinit> 
SourceFile &CrossDomainScriptInclusionScanner.java 9 : 5 6 � � � � � _ � � � � � script � � � � � � � � � � � net/htmlparser/jericho/Element � � � � � � a b c W X 'org/parosproxy/paros/core/scanner/Alert ^ _ ` a 9 � YThe page at the following URL includes one or more script files from a third-party domain � � � a   �Ensure JavaScript source files are loaded from only trusted sources, and the sources can't be controlled by end users of the application � � � W � -Cross-domain JavaScript source file inclusion // � � � / ./ ../ !org/apache/commons/httpclient/URI 9 � � a � a � � *org/apache/commons/httpclient/URIException 7 8 java/lang/StringBuilder Error:  � � � a � � � Forg/zaproxy/zap/extension/pscanrules/CrossDomainScriptInclusionScanner � � 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/List java/util/Iterator (org/parosproxy/paros/network/HttpMessage getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody length getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader isText ()Z net/htmlparser/jericho/Source getAllElements $(Ljava/lang/String;)Ljava/util/List; iterator ()Ljava/util/Iterator; hasNext next ()Ljava/lang/Object; getAttributeValue &(Ljava/lang/String;)Ljava/lang/String; getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getHostName (IIILjava/lang/String;)V getURI %()Lorg/apache/commons/httpclient/URI; toString 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V java/lang/String 
startsWith (Ljava/lang/String;)Z (Ljava/lang/String;Z)V getHost toLowerCase equals (Ljava/lang/Object;)Z append -(Ljava/lang/String;)Ljava/lang/StringBuilder; 
getMessage org/apache/log4j/Logger debug (Ljava/lang/Object;)V 	getLogger ,(Ljava/lang/Class;)Lorg/apache/log4j/Logger; ! 2 4     5 6    7 8   	  9 :  ;   8     
*� *� �    <   
    !  # =       
 > ?    @ A  ;   ?      �    <       ) =         > ?      B C     D E   F G  ;  !     j+� � � b+� � � X-� :� K� 	 :� 
 � 8�  � :� :� *+� � � � *+� ��ı    <   & 	   -  .  / ! 0 @ 1 I 2 ^ 3 f 5 i 8 =   R  I  H I  @ & J K  * ? L M   M N O    j > ?     j B C    j D E    j P Q  R      M N S  T    � * U V;�   W X  ;   � 	    9� Y*� *� � :+� � � -+� *� � �    <       ;  = . G 8 H =   4    9 > ?     9 B C    9 D E    9 Y I   & Z [   \ ]  ;   >     *+� �    <   
    L  M =        > ?      5 6   ^ _  ;   .     '!�    <       P =        > ?    ` a  ;   -     �    <       U =        > ?    b c  ;       t,�  �  ,!�  � ,"�  � ,#�  � �>� $Y,� %:� &:� � '+� '� (� >� ":� *� +Y� ,-� .� /� .� 0� 1�  ( P S )  <   .    Y $ Z & \ ( ^ 3 _ : ` N a P e S c U d r f =   H  3  d e  :  f I  U  g h    t > ?     t i I    t j I  ( L k l  T    $� )B m  n :  ;   "      
 2� 3� *�    <       $  o    pPK
    �K�Au��_S  S  E   org/zaproxy/zap/extension/pscanrules/HeaderXssProtectionScanner.class����   3 �
   K	  L
 M N
 O P
 M Q
 R S T
 R U
 V W X Y X Z [
  \ ]
  ^
  _ `
  a
  b
  c d
 M e
 f g
 h i j k l
  m
 n o p q r parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; <init> ()V Code LineNumberTable LocalVariableTable this ALorg/zaproxy/zap/extension/pscanrules/HeaderXssProtectionScanner; 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V xssHeaderProtectionParam Ljava/lang/String; i$ Ljava/util/Iterator; xssHeaderProtection Ljava/util/Vector; source Lnet/htmlparser/jericho/Source; LocalVariableTypeTable &Ljava/util/Vector<Ljava/lang/String;>; StackMapTable s t 
raiseAlert @(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;)V alert )Lorg/parosproxy/paros/core/scanner/Alert; getId ()I getName ()Ljava/lang/String; 
SourceFile HeaderXssProtectionScanner.java # $ ! " u v w x y F z { | } ~ X-XSS-Protection  � s � � t � ~ � � java/lang/String � H 1 � � A B 'org/parosproxy/paros/core/scanner/Alert E F G H # � DThe x-xss-protection header has been disabled by the web application � � � � � � � H   �Enable the IE's XSS filter. If it must be disabled for any reasons, ensure that the application is properly protected against XSS vulnerability Qhttps://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet � � � A � (IE8's XSS protection filter not disabled ?org/zaproxy/zap/extension/pscanrules/HeaderXssProtectionScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/Vector java/util/Iterator (org/parosproxy/paros/network/HttpMessage getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody length getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader isText ()Z 
getHeaders &(Ljava/lang/String;)Ljava/util/Vector; iterator ()Ljava/util/Iterator; hasNext next ()Ljava/lang/Object; toLowerCase indexOf (Ljava/lang/String;)I (IIILjava/lang/String;)V getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI toString 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V !        ! "     # $  %   8     
*� *� �    &   
       '       
 ( )    * +  %   >     *+� �    &   
    #  $ '        ( )      ! "   , -  %   ?      �    &       ) '         ( )      . /     0 1   2 3  %       Z+� � � R+� � � H+� � :� 8� 	:� 
 � '�  � :� � � *+� ��ձ    &   "    -  .  / $ 0 A 1 N 2 V 4 Y 7 '   H  A  4 5  + . 6 7   : 8 9    Z ( )     Z . /    Z 0 1    Z : ;  <      : 8 =  >    � + ? @*�   A B  %   � 	    9� Y*� *� � :+� � � -+� *� � �    &       :  < . F 8 G '   4    9 ( )     9 . /    9 0 1    9 8 5   & C D   E F  %   .     ' �    &       J '        ( )    G H  %   -     �    &       O '        ( )    I    JPK
    �K�A�����  �  @   org/zaproxy/zap/extension/pscanrules/MixedContentScanner$1.class����   3 /
  	  	  
   	    " B$SwitchMap$org$parosproxy$paros$core$scanner$Plugin$AlertThreshold [I <clinit> ()V Code LineNumberTable LocalVariableTable ex Ljava/lang/NoSuchFieldError; StackMapTable  
SourceFile MixedContentScanner.java EnclosingMethod # % ' ( 	 
 ) * + , java/lang/NoSuchFieldError - * :org/zaproxy/zap/extension/pscanrules/MixedContentScanner$1   InnerClasses java/lang/Object 8org/zaproxy/zap/extension/pscanrules/MixedContentScanner . 7org/parosproxy/paros/core/scanner/Plugin$AlertThreshold AlertThreshold values <()[Lorg/parosproxy/paros/core/scanner/Plugin$AlertThreshold; LOW 9Lorg/parosproxy/paros/core/scanner/Plugin$AlertThreshold; ordinal ()I MEDIUM (org/parosproxy/paros/core/scanner/Plugin       	 
           ~     (� ��
� � � � O� K� � � O� K�  	     # &          I             '           W  M                !          $ &@PK
    �K�A�JdI  I  K   org/zaproxy/zap/extension/pscanrules/MixedContentScanner$MixedContent.class����   3 '	  
  	  	   	  ! # $ tag Ljava/lang/String; att value this$0 :Lorg/zaproxy/zap/extension/pscanrules/MixedContentScanner; <init> s(Lorg/zaproxy/zap/extension/pscanrules/MixedContentScanner;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V Code LineNumberTable LocalVariableTable this MixedContent InnerClasses GLorg/zaproxy/zap/extension/pscanrules/MixedContentScanner$MixedContent; getTag ()Ljava/lang/String; getAtt getValue 
SourceFile MixedContentScanner.java    %  	 
 	  	 & Eorg/zaproxy/zap/extension/pscanrules/MixedContentScanner$MixedContent java/lang/Object ()V 8org/zaproxy/zap/extension/pscanrules/MixedContentScanner          	    
 	     	               v     *+� *� *,� *-� *� �           �  � 	 �  �  �  �    *             	     
 	      	         /     *� �           �                    /     *� �           �                    /     *� �           �                      
   "  PK
    �K�ARl`  `  >   org/zaproxy/zap/extension/pscanrules/MixedContentScanner.class����   3
 D �	 C �
 � �
 � � �
  �
 � �
 � �
 � �
 � �
 � � � � � � � � � �
 C � �
  �
 � � � � � � � �	 E �
 C �
 � � � � � � �
 ! � � �
 ! �
 # � �
 # � �
 # � � � �
 ! �
 C �
  �
 � � �
 � �
 # � � � � � �
 C �
 7 � �
 � �
 � � F � �
 7 �
 � � � � � �   InnerClasses MixedContent parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; <init> ()V Code LineNumberTable LocalVariableTable this :Lorg/zaproxy/zap/extension/pscanrules/MixedContentScanner; 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V sourceElement  Lnet/htmlparser/jericho/Element; i$ Ljava/util/Iterator; mc GLorg/zaproxy/zap/extension/pscanrules/MixedContentScanner$MixedContent; sb Ljava/lang/StringBuffer; sourceElements Ljava/util/List; source Lnet/htmlparser/jericho/Source; list 	incScript Z LocalVariableTypeTable 2Ljava/util/List<Lnet/htmlparser/jericho/Element;>; YLjava/util/List<Lorg/zaproxy/zap/extension/pscanrules/MixedContentScanner$MixedContent;>; StackMapTable � � � � � � � addAttsContainingHttpContent E(Lnet/htmlparser/jericho/Element;Ljava/lang/String;Ljava/util/List;)Z 	attribute Ljava/lang/String; val � 	Signature �(Lnet/htmlparser/jericho/Element;Ljava/lang/String;Ljava/util/List<Lorg/zaproxy/zap/extension/pscanrules/MixedContentScanner$MixedContent;>;)Z 
raiseAlert S(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;Ljava/lang/String;Z)V first all name risk alert )Lorg/parosproxy/paros/core/scanner/Alert; getId ()I getName ()Ljava/lang/String; 
SourceFile MixedContentScanner.java K L I J � � � � � � java/util/ArrayList � � � � � � � � � � � � � � � � � � � � � net/htmlparser/jericho/Element src v w script � � � � � 
background classid codebase data icon usemap � � � � � � � action 
formaction � � java/lang/StringBuffer Eorg/zaproxy/zap/extension/pscanrules/MixedContentScanner$MixedContent tag= � � � �   � � =  � 
 � ~  � http: K	
 � "Secure page includes mixed content 5Secure page includes mixed content, including scripts 'org/parosproxy/paros/core/scanner/Alert � � K OThe page includes mixed content, ie content accessed via http instead of https. �A page that is available over TLS must be comprised completely of content which is transmitted over TLS. 
The page must not contain any content that is transmitted over unencrypted HTTP.
This includes content from unrelated third party sites. Fhttps://www.owasp.org/index.php/Transport_Layer_Protection_Cheat_Sheet ~ $Secure pages including mixed content 8org/zaproxy/zap/extension/pscanrules/MixedContentScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner :org/zaproxy/zap/extension/pscanrules/MixedContentScanner$1 (org/parosproxy/paros/network/HttpMessage net/htmlparser/jericho/Source java/util/List java/util/Iterator java/lang/String getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader 	getSecure ()Z getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody length getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader isText getAllElements ()Ljava/util/List; iterator ()Ljava/util/Iterator; hasNext next ()Ljava/lang/Object; equals (Ljava/lang/Object;)Z B$SwitchMap$org$parosproxy$paros$core$scanner$Plugin$AlertThreshold [I getLevel AlertThreshold ;()Lorg/parosproxy/paros/core/scanner/Plugin$AlertThreshold; 7org/parosproxy/paros/core/scanner/Plugin$AlertThreshold ordinal size append ,(Ljava/lang/String;)Ljava/lang/StringBuffer; getTag getAtt getValue get (I)Ljava/lang/Object; toString getAttributeValue &(Ljava/lang/String;)Ljava/lang/String; toLowerCase 
startsWith (Ljava/lang/String;)Z s(Lorg/zaproxy/zap/extension/pscanrules/MixedContentScanner;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V add (IIILjava/lang/String;)V getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V (org/parosproxy/paros/core/scanner/Plugin ! C D     I J     K L  M   <     
*� *� �    N       !  # 	 � O       
 P Q    R S  M   >     *+� �    N   
    '  ( O        P Q      I J   T U  M   ?      �    N       - O         P Q      V W     X Y   Z [  M    
  �+� � � �� Y� :6+� � �g+� 	� 
�]-� :� ��  :�  � ��  � :*� � � � � 6*� W*� W*� W*� W*� W*� W� *� � .�   2               *� W*� W� ��N�   � �� !Y� ":�  :�  � S�  � #:	$� %W	� &� %W'� %W	� (� %W)� %W	� *� %W+� %W���*+� , � #� *� -� .�    N   � #   1 
 3  5  6  8 + 9 1 : 6 ; U < b = o ? r B } C � D � E � F � G � I � M � N � O � S � U � V W& X. Y9 ZA [L \T ]_ ^g _j a� d O   z  U � \ ]  ? � ^ _ & A ` a 	 Z ^ _  ~ b c  1T d e   � P Q    � V W   � X Y   � f g  r h e  o i j  k     1T d l  r h m  n   < 	� 3  o p q r r s  � 2 t� e� � �  u s� Y�   v w  M   �     3+,� /:� (� 01� 2� -� #Y*+� ,� 3� 4 W��    N       g  h  i / j 1 l O   4    3 P Q     3 \ ]    3 x y    3 h e   , z y  k       3 h m  n    � 1 { |    }  ~   M   � 	 	   K5:6� 
6:6� 7Y*� 8� 9::+� � ;� <=->?+� @*� � A�    N   & 	   p  q  r  s  t  v % w @ � J � O   \ 	   K P Q     K V W    K X Y    K � y    K � y    K i j   G � y   D � Y  % & � �  n   	 �  {  � �  M   .     '�    N       � O        P Q    � �  M   -     B�    N       � O        P Q    �    � G     E C   # C H  � � �@PK
    �K�Ay�)I    F   org/zaproxy/zap/extension/pscanrules/PasswordAutocompleteScanner.class����   3 �
 5 m	 4 n
 o p
 q r	 4 s t
  m u
 v w x y z
  m {
  |
  } ~
  
 q � x � � � � � � �
  � �
 � � �
  w � � � �
 4 � �
   � �
 � �
 � �
 � 
  � � � �
   �
 � �
 q � � �
  �
  � � � � parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; logger Lorg/apache/log4j/Logger; <init> ()V Code LineNumberTable LocalVariableTable this BLorg/zaproxy/zap/extension/pscanrules/PasswordAutocompleteScanner; 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I getId ()I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V alert )Lorg/parosproxy/paros/core/scanner/Alert; type Ljava/lang/String; inputElement  Lnet/htmlparser/jericho/Element; i$ Ljava/util/Iterator; inputElements Ljava/util/List; autoComplete formElement source Lnet/htmlparser/jericho/Source; start Ljava/util/Date; formElements LocalVariableTypeTable 2Ljava/util/List<Lnet/htmlparser/jericho/Element;>; StackMapTable t � � � � � � � getName ()Ljava/lang/String; 
SourceFile  PasswordAutocompleteScanner.java : ; 6 7 � � � � � � 8 9 java/util/Date form � � � � � J java/lang/StringBuilder Found  � � � �  forms � j � � � � � � � � � net/htmlparser/jericho/Element AUTOCOMPLETE � � OFF � � � input  inputs TYPE PASSWORD 'org/parosproxy/paros/core/scanner/Alert I J  Password Autocomplete in browser : � �AUTOCOMPLETE attribute is not disabled in HTML FORM/INPUT element containing password type input.  Passwords may be stored in browsers and retrieved. � � � � � � � i j   tTurn off AUTOCOMPLETE attribute in form or individual input elements containing password by using AUTOCOMPLETE='OFF' ]http://msdn.microsoft.com/library/default.asp?url=/workshop/author/forms/autocomplete_ovr.asp � � � � � � � 	Scan of record   took  � � � �  ms @org/zaproxy/zap/extension/pscanrules/PasswordAutocompleteScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/List java/util/Iterator java/lang/String (org/parosproxy/paros/network/HttpMessage net/htmlparser/jericho/Source java/lang/Object getClass ()Ljava/lang/Class; org/apache/log4j/Logger 	getLogger ,(Ljava/lang/Class;)Lorg/apache/log4j/Logger; getAllElements $(Ljava/lang/String;)Ljava/util/List; size append -(Ljava/lang/String;)Ljava/lang/StringBuilder; (I)Ljava/lang/StringBuilder; toString debug (Ljava/lang/Object;)V iterator ()Ljava/util/Iterator; hasNext ()Z next ()Ljava/lang/Object; getAttributeValue &(Ljava/lang/String;)Ljava/lang/String; equalsIgnoreCase (Ljava/lang/String;)Z (IIILjava/lang/String;)V getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread 
raiseAlert -(ILorg/parosproxy/paros/core/scanner/Alert;)V isDebugEnabled getTime ()J (J)Ljava/lang/StringBuilder; ! 4 5     6 7    8 9     : ;  <   G     *� *� **� � � �    =       "  $ 	 % >        ? @    A B  <   >     *+� �    =   
    )  * >        ? @      6 7   C D  <   ?      �    =       / >         ? @      E F     G H   I J  <   .     '�    =       2 >        ? @    K L  <  � 	   �� Y� :-� 	:�5� 
 �+*� � Y� � � 
 � � � � �  :�  � ��  � :� :� � � �� :		� �	� 
 � �*� � Y� � 	� 
 � � � � 	�  :

�  � |
�  � :� :� _� � U� :� � � =�  Y*� !"� #:$+� %� &� '� ())*++� ,*� � -�����	*� � .� :*� � Y� /� � 0� � Y� � 1� 1e� 23� � � �    =   ^    7 	 9  ;   = E ? d @ m B | D � F � H � I � J � K � M � N P R8 \B _E bH dR e� h >   �  ) M N  � a O P  � j Q R  � � S T 
 � � U V 	 m � W P  d � X R  N � S T   � ? @    � E F   � G H   � Y Z  	� [ \  y ] V  ^     � � U _ 	 y ] _  `   L � N a b c� - d e� E b c� E d e� 9�   f g h a b c  � � @  i j  <   -     "�    =       l >        ? @    k    lPK
    �K�A5B��>  >  E   org/zaproxy/zap/extension/pscanrules/XContentTypeOptionsScanner.class����   3 �
  J	  K
 L M
 N O
 L P Q
 R S T
  U
 V W X Y X Z [
  \ ]
  ^ _
  `
  a
  b c
 L d
 e f
 g h i
  j
 k l m n o parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; <init> ()V Code LineNumberTable LocalVariableTable this ALorg/zaproxy/zap/extension/pscanrules/XContentTypeOptionsScanner; scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V xContentTypeOptionsDirective Ljava/lang/String; i$ Ljava/util/Iterator; xContentTypeOptions Ljava/util/Vector; source Lnet/htmlparser/jericho/Source; LocalVariableTypeTable &Ljava/util/Vector<Ljava/lang/String;>; StackMapTable p q 
raiseAlert @(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;)V xContentTypeOption alert )Lorg/parosproxy/paros/core/scanner/Alert; 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V getName ()Ljava/lang/String; getId ()I 
SourceFile XContentTypeOptionsScanner.java ! "    r s t u v G w x X-Content-Type-Options y z {   = > p | } q ~  � � java/lang/String � E nosniff � � 'org/parosproxy/paros/core/scanner/Alert F G D E ! � MThe Anti-MIME-Sniffing header X-Content-Type-Options was not set to 'nosniff' � � � � � � � E �This check is specific to Internet Explorer 8 and Google Chrome. Ensure each page sets a Content-Type header and the X-CONTENT-TYPE-OPTIONS if the Content-Type header is unknown � � � = � %X-Content-Type-Options header missing ?org/zaproxy/zap/extension/pscanrules/XContentTypeOptionsScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/Vector java/util/Iterator (org/parosproxy/paros/network/HttpMessage getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody length getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader 
getHeaders &(Ljava/lang/String;)Ljava/util/Vector; iterator ()Ljava/util/Iterator; hasNext ()Z next ()Ljava/lang/Object; toLowerCase indexOf (Ljava/lang/String;)I (IIILjava/lang/String;)V getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI toString 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V !              ! "  #   8     
*� *� �    $   
       %       
 & '    ( )  #   ?      �    $        %         & '      * +     , -   . /  #       [+� � � S+� � :� *+� 	� 8� 
:�  � '�  � :� � � *+� 	��ձ    $   & 	    
      %  B  O   W " Z % %   H  B  0 1  , . 2 3   E 4 5    [ & '     [ * +    [ , -    [ 6 7  8      E 4 9  :    � % ;�  <*�   = >  #   � 	    9� Y*� *� � :+� � � -+� *� � �    $       (  * . 4 8 5 %   4    9 & '     9 * +    9 , -    9 ? 1   & @ A   B C  #   >     *+� �    $   
    :  ; %        & '           D E  #   -     �    $       ? %        & '    F G  #   .     '%�    $       C %        & '    H    IPK
    �K�A���    >   org/zaproxy/zap/extension/pscanrules/XFrameOptionScanner.class����   3 �
 " Q	 ! R
 S T
 U V
 S W
 X Y Z
 X [
 \ ] ^ _ ^ ` a
  b c
  d e
 ! f g h i j
 ! k
 ! l
  m
 S n
 o p
 q r s t
  u
 v w x y z parent 3Lorg/zaproxy/zap/extension/pscan/PassiveScanThread; <init> ()V Code LineNumberTable LocalVariableTable this :Lorg/zaproxy/zap/extension/pscanrules/XFrameOptionScanner; scanHttpRequestSend .(Lorg/parosproxy/paros/network/HttpMessage;I)V msg *Lorg/parosproxy/paros/network/HttpMessage; id I scanHttpResponseReceive M(Lorg/parosproxy/paros/network/HttpMessage;ILnet/htmlparser/jericho/Source;)V xFrameOptionParam Ljava/lang/String; i$ Ljava/util/Iterator; xFrameOption Ljava/util/Vector; source Lnet/htmlparser/jericho/Source; LocalVariableTypeTable &Ljava/util/Vector<Ljava/lang/String;>; StackMapTable { | 
raiseAlert A(Lorg/parosproxy/paros/network/HttpMessage;ILjava/lang/String;Z)V isXFrameOptionsMissing Z issue alert )Lorg/parosproxy/paros/core/scanner/Alert; a 	setParent 6(Lorg/zaproxy/zap/extension/pscan/PassiveScanThread;)V getName ()Ljava/lang/String; getId ()I 
SourceFile XFrameOptionScanner.java % & # $ } ~  � � N � � � � � X-Frame-Options � � { � � | � � � � java/lang/String � L deny � � 
sameorigin A B   MX-Frame-Options header was not set for defense against 'ClickJacking' attacks eX-Frame-Options header is not included in the HTTP response to protect against 'ClickJacking' attacks 'org/parosproxy/paros/core/scanner/Alert M N K L % � � � � � � � � LHMost modern Web browsers support the X-Frame-Options HTTP header, ensure it's set on all web pages returned by your site (if you expect the page to be framed only by pages on your server (e.g. it's part of a FRAMESET) then you'll want to use SAMEORIGIN, otherwise if you never expect the page to be framed, you should use DENY). whttp://blogs.msdn.com/b/ieinternals/archive/2010/03/30/combating-clickjacking-with-x-frame-options.aspx?Redirected=true � � � A � X-Frame-Options header not set 8org/zaproxy/zap/extension/pscanrules/XFrameOptionScanner 4org/zaproxy/zap/extension/pscan/PluginPassiveScanner java/util/Vector java/util/Iterator (org/parosproxy/paros/network/HttpMessage getResponseBody ,()Lorg/zaproxy/zap/network/HttpResponseBody; (org/zaproxy/zap/network/HttpResponseBody length getResponseHeader 3()Lorg/parosproxy/paros/network/HttpResponseHeader; /org/parosproxy/paros/network/HttpResponseHeader isText ()Z 
getHeaders &(Ljava/lang/String;)Ljava/util/Vector; iterator ()Ljava/util/Iterator; hasNext next ()Ljava/lang/Object; toLowerCase indexOf (Ljava/lang/String;)I (IIILjava/lang/String;)V getRequestHeader 2()Lorg/parosproxy/paros/network/HttpRequestHeader; .org/parosproxy/paros/network/HttpRequestHeader getURI %()Lorg/apache/commons/httpclient/URI; !org/apache/commons/httpclient/URI toString 	setDetail �(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/parosproxy/paros/network/HttpMessage;)V 1org/zaproxy/zap/extension/pscan/PassiveScanThread -(ILorg/parosproxy/paros/core/scanner/Alert;)V ! ! "     # $     % &  '   8     
*� *� �    (   
       )       
 * +    , -  '   ?      �    (       # )         * +      . /     0 1   2 3  '  %     t+� � � l+� � � b+� � :� I� 	:� 
 � 5�  � :� � � � � � *+� ��ǧ *+� �    (   & 	   '  (  ) $ * A + [ , d . j 0 s 3 )   H  A # 4 5  + < 6 7   T 8 9    t * +     t . /    t 0 1    t : ;  <      T 8 =  >    � + ? @8� �   A B  '   � 	    F:� :� Y*� *� � :+� � � -+� *� � �    (       6  7 	 8  :   < ; F E G )   H    F * +     F . /    F 0 1    F 8 5    F C D   B E 5    & F G  >    �  H  I J  '   >     *+� �    (   
    K  L )        * +      # $   K L  '   -      �    (       P )        * +    M N  '   .     '$�    (       T )        * +    O    PPK
    �K�A            	         �A    META-INF/��  PK
    �K�A�?�~g   g              ��+   META-INF/MANIFEST.MFPK
    �K�A                      �A�   org/PK
    �K�A                      �A�   org/zaproxy/PK
    �K�A                      �A  org/zaproxy/zap/PK
    �K�A                      �A>  org/zaproxy/zap/extension/PK
    �K�A            %          �Av  org/zaproxy/zap/extension/pscanrules/PK
    �K�A{9     >           ���  org/zaproxy/zap/extension/pscanrules/CacheControlScanner.classPK
    �K�AX��  �  D           ��&  org/zaproxy/zap/extension/pscanrules/ContentTypeMissingScanner.classPK
    �K�A=��Ă  �  @           ��3   org/zaproxy/zap/extension/pscanrules/CookieHttpOnlyScanner.classPK
    �K�An���  �  B           ��/  org/zaproxy/zap/extension/pscanrules/CookieSecureFlagScanner.classPK
    �K�A�o�R`  `  L           ��A>  org/zaproxy/zap/extension/pscanrules/CrossDomainScriptInclusionScanner.classPK
    �K�Au��_S  S  E           ��R  org/zaproxy/zap/extension/pscanrules/HeaderXssProtectionScanner.classPK
    �K�A�����  �  @           ���`  org/zaproxy/zap/extension/pscanrules/MixedContentScanner$1.classPK
    �K�A�JdI  I  K           ��e  org/zaproxy/zap/extension/pscanrules/MixedContentScanner$MixedContent.classPK
    �K�ARl`  `  >           ���i  org/zaproxy/zap/extension/pscanrules/MixedContentScanner.classPK
    �K�Ay�)I    F           ��t�  org/zaproxy/zap/extension/pscanrules/PasswordAutocompleteScanner.classPK
    �K�A5B��>  >  E           ���  org/zaproxy/zap/extension/pscanrules/XContentTypeOptionsScanner.classPK
    �K�A���    >           ����  org/zaproxy/zap/extension/pscanrules/XFrameOptionScanner.classPK        ��    