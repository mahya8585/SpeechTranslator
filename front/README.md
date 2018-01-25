## 使う前にやるべきこと

① npmしる。   
- redis   
- socket.io
- express

② redis接続先設定を書き換える      
各JS内の接続先設定（主にRedis)を書き換える。   
e.g.)   
var client = require('redis').createClient(6380,'foo.bar.server.net', {auth_pass: 'ffqqwfqwfqqah4357=', tls: {servername: 'foo.bar.server.net'}});   
   
   
## 作ったテストアプリ
① 各フォルダ内のjsをnodeで実行する。   
e.g.) node app.js   

② htmlが付属されている場合はブラウザから対象ポートに接続する   
e.g.) 以下URLで接続できるようにしてあります   
http://localhost:3001/   
   


