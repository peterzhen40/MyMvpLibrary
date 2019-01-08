### lib_base
基础Library，存放一些公共方法、公共常量、组件通信的契约类等。上层被所有组件依赖，下层依赖公共资源库、路由库等通用库，
通过它，避免了组件直接依赖各种通用库，承上启下，作为整个组件化的核心库。

### 说明
1. BaseActivity/BaseFragment常用方法写在代理类里，不要写在IView

### PS
谨慎更改这个库的代码！！！