# Gradle入門

## 環境
---
### OS
* Windows 7 64bit

### Gradle
* 2.10


## 初期設定
---
### gradleにパスを通しましょう
環境変数に`C:\HatenaBlogManagerまでのパス\gradle-2.10\bin`を追加しましょう。

環境変数が分からない場合は`環境変数`をGoogle先生に聞いてみて下さい。


### gradleにパスが通ったか確認しましょう
`windows+R`で検索が立ちあがります。

`cmd`と入力してコマンドプロンプトを立ち上げましょう。

コマンドプロンプトに`gradle --version`と打ち込んでgredleのバージョンが表示されたら成功です。


## 実践
---
### gradleでビルドしてみよう
ここまで成功したら実践あるのみ

コマンドプロンプトで`C:\HatenaBlogManagerまでのパス`まで移動しましょう。

そのディレクトリで`gradle build`と入力。

`BUILD SUCCESSFUL`と表示されたらビルド成功です。

表示されない場合は`gradle compileJava`を入力してみましょう。


## 参考
* [Gradle使い方メモ](http://qiita.com/opengl-8080/items/4c1aa85b4737bd362d9e)
