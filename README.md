# 基于 ijkPlayer 的网络视频播放应用

## 展示

![player](showPicture/Player.gif)

## 使用技术

- 播放器底层使用 ijkPlayer，并争对 H264 编码的视频，在 I 帧处解密
- 播放器使用自定义 View 封装，采用 MVVM 设计模式，支持暂停、重播、滑动手势、旋转播放
- 使用 OkHttp 框架与服务器通信获取视频信息
