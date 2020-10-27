# SafeKeyboard
基于Android开发的安全键盘


1.考虑root和xposed相关检查

2.不管是dialog还是popwindow都需要禁止截屏

3.EditText禁止黏贴

3.EditText不设置inputType为password，防止给hook直接暴露明文出来，采取用*替换·

4.输入到EditText的内容要做一个影射关系，即使给人hook textchange也拿不到真实的内容

5.提交表单的时候，进行安全键盘解密，防止给辅助功能或者xpose强行黏贴进来

6.加解密的算法尽量用c来实现，防止java层直接给xpose hook查看到加密方式

7.针对so进行ollvm处理，防止给人直接dlopen函数 进行hook

以上内容通过分析xx软件的安全键盘分析出来的，后续考虑怎么实现
