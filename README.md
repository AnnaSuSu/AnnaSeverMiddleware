# AnnaSeverMiddleware
各种封装的中间件。Netyy，Mina，WebSocket，Spring，Spring Boot，Shiro


# Netty

Netty整合了自定义协议处理，对于粘包拆包的处理，WebSocket协议，因为要跟硬件进行通信，硬件用TCP协议发16进制的字节包过来以后，Netty进行处理，在通过自带的WebSocket协议推送给前端界面，从而达到了试试看版的效果


Netty自定义协议测试包：FE0313011455


FE：包头
55：包围
03：有效数据字节数

