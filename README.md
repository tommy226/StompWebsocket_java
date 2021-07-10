# StompWebsocket_java

stomp websocket client 심플 메세지 (java)

### Library

* stomp websocket : 메세지 소켓 통신
* RXjava, RXandroid : 구독 필요
* Gson : json 객체 클래스 컨버팅

사용자는 방과 이름을 입력하여 채팅방에 참가한다.

room은 소켓 주소가 되며
name은 사용자 계정이 된다.

``` java

 //example subscribe
  mStompClient.topic("/hello + "/" + {room}).subscribe(stompMessage -> {     
            Log.d(TAG, "receive messageData :" + stompMessage.getPayload());
            
 //example send
   mStompClient.send("/hello + "/" + {room}, messageJson).subscribe();
        Log.d(TAG, "send messageData : " + messageJson);
```


#### 심플 메세지 스크린샷

<img src="https://ifh.cc/g/xHEj7w.jpg" width="30%" height="20%"></img>
방과 자신의 이름 입력 

<img src="https://ifh.cc/g/la3KWf.jpg" width="30%" height="20%"></img> 
성빈(나) 덕배(상대) 와 채팅

<img src="https://ifh.cc/g/LfQiXa.jpg" width="30%" height="20%"></img>
덕배의 관점

<img src="https://ifh.cc/g/vtEAhH.jpg" width="30%" height="20%"></img> 
여러명과 채팅 가능


##### 참조
[stomp github][link]

[link]: https://github.com/NaikSoftware/StompProtocolAndroid
