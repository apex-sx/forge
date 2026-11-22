# Git 提交规范文档

> **版本**：v1.0　**适用范围**：全部业务仓库、脚手架、Lib 与内部工具仓库
> **规范依据**：Conventional Commits 1.0.0 + Angular Commit Message 约定
> **维护责任**：前端/后端研发组共同维护，变更需走 PR 评审

---

## 目录

1. [规范目的与适用范围](#一规范目的与适用范围)
2. [提交信息整体结构](#二提交信息整体结构)
3. [type 类型约定](#三type-类型约定)
4. [scope 作用域约定](#四scope-作用域约定)
5. [description 书写规则](#五description-书写规则)
6. [body 正文写法](#六body-正文写法)
7. [footer 脚注写法](#七footer-脚注写法)
8. [破坏性变更（BREAKING CHANGE）](#八破坏性变更breaking-change)
9. [与语义化版本（SemVer）的对应关系](#九与语义化版本semver的对应关系)
10. [正例与反例大全](#十正例与反例大全)
11. [提交粒度与拆分原则](#十一提交粒度与拆分原则)
12. [分支命名规范](#十二分支命名规范)
13. [工具链落地配置](#十三工具链落地配置)
14. [Pull Request 与合并规范](#十四pull-request-与合并规范)
15. [常见问题 FAQ](#十五常见问题-faq)
16. [附录](#十六附录)

---

## 一、规范目的与适用范围

### 1.1 为什么要统一提交信息

| 问题 | 影响 | 统一规范后 |
| --- | --- | --- |
| 提交信息随意（"改bug""优化"） | 历史不可读，排查问题靠猜 | 一眼看出改动性质与范围 |
| 类型混杂 | 无法自动生成 CHANGELOG | 可按 feat/fix 自动归类 |
| 一个 commit 改十个模块 | 无法精准回滚、cherry-pick | 粒度可回滚、可评审 |
| 无破坏性变更标记 | 升级依赖后线上炸 | 升级前可自动识别风险 |

### 1.2 适用范围

- 所有纳入 Git 管理的代码仓库（含前端、后端、SDK、脚本、配置仓库）；
- 所有参与提交的人员：正式员工、外包、实习、自动化机器人（bot 提交也应遵循）；
- 不强制约束 **本地临时分支的中间调试提交**，但合入主干前必须整理（见第 14 章）。

### 1.3 硬性要求（MUST）与建议（SHOULD）

- **MUST**：Header 格式合法，type 取值在枚举内，description 非空；
- **MUST**：破坏性变更必须显式标注 `!` 或 `BREAKING CHANGE:`；
- **MUST**：一个 commit 只解决一件事；
- **SHOULD**：附带 scope，便于按模块过滤；
- **SHOULD**：关联对应 Issue / 需求单；
- **MAY**：中文或英文描述，但 **同一仓库必须统一**。

---

## 二、提交信息整体结构

```
<type>[optional scope]: <description>
<空行>
[optional body]
<空行>
[optional footer(s)]
```

带破坏性标记时的 Header 形式：

```
<type>[(scope)]!: <description>
```

### 2.1 结构说明

| 部分 | 是否必填 | 位置 | 说明 |
| --- | --- | --- | --- |
| type | 必填 | Header | 变更类别，小写 |
| scope | 可选 | Header，括号内 | 变更涉及的模块/包名 |
| `!` | 可选 | type 或 scope 之后、冒号之前 | 破坏性变更标记 |
| description | 必填 | Header，冒号后 | 一句话说明改动 |
| body | 可选 | Header 后空一行 | 详细动机、方案、影响面 |
| footer | 可选 | body 后空一行 | Issue 关联、破坏性说明、评审引用 |

### 2.2 格式硬约束

1. Header 与 body、body 与 footer 之间 **必须空一行**，否则解析器无法识别；
2. Header 冒号使用 **英文半角冒号 + 一个空格**（`: `），禁止中文全角冒号；
3. Header 建议不超过 **72 个字符**（中文不超过 50 个汉字），便于 `git log --oneline` 单行展示；
4. 不允许以句号结尾；
5. 不允许出现 type 大写（`Feat`、`FIX` 均不合法）；
6. 多行 footer 之间用空行分隔，每个 token 形如 `Token: value`。

---

## 三、type 类型约定

### 3.1 标准枚举（固定，不可自造）

| type | 使用场景 | 是否出现在 CHANGELOG | 版本影响 |
| --- | --- | --- | --- |
| `feat` | 新增面向用户的功能、接口、能力 | 是（Features） | MINOR |
| `fix` | 修复缺陷、错误行为、线上故障 | 是（Bug Fixes） | PATCH |
| `docs` | 文档、README、注释、示例代码说明 | 否 | 无 |
| `style` | 格式调整：空格、缩进、分号、换行、lint 自动修复 | 否 | 无 |
| `refactor` | 代码重构：不新增功能、不修复 bug | 视配置 | 无 |
| `perf` | 性能优化：减少耗时、内存、包体积 | 是（Performance） | PATCH |
| `test` | 新增/修改测试用例、测试配置 | 否 | 无 |
| `build` | 构建系统、打包配置、依赖版本（webpack/vite/pnpm lock） | 否 | 视依赖 |
| `ci` | CI/CD 流水线、部署脚本、自动化配置 | 否 | 无 |
| `chore` | 杂项：`.gitignore`、脚手架初始化、非业务配置 | 否 | 无 |
| `revert` | 回滚某次历史提交 | 是（Reverts） | 按被回滚类型 |

> 说明：`build` 与 `chore` 界限模糊时，涉及"依赖版本/构建产物/发布流程"选 `build`，"不影响产物与依赖的杂事"选 `chore`。团队若使用 Angular 9 之前的老枚举，可将 `build`/`ci` 合并进 `chore`，但需在本仓库 README 中注明。

### 3.2 type 选择决策流程

```
改动是否修复了错误行为？        → 是 → fix
改动是否新增了用户可见能力？    → 是 → feat
改动是否只涉及文档/注释？       → 是 → docs
改动是否只涉及空格格式？        → 是 → style
改动是否只涉及测试代码？        → 是 → test
改动是否只提升性能、不改行为？  → 是 → perf
改动是否只调整依赖/构建/CI？    → 是 → build / ci
改动是否重构结构、行为不变？    → 是 → refactor
以上都不是（杂项小改）          → chore
回滚历史提交                   → revert
```

### 3.3 常见误用

- ❌ 用 `fix` 提交新增的小功能（应 `feat`，否则 CHANGELOG 归类错误）；
- ❌ 用 `update`/`change`/`modify` 这类非枚举词（解析器会丢弃该提交）；
- ❌ 用 `feat` 提交纯格式化（污染 Features 列表）；
- ❌ 用 `refactor` 掩盖行为改变（重构的硬定义是"外部可观察行为不变"）。

---

## 四、scope 作用域约定

### 4.1 定义原则

scope 描述 **改动落在哪个模块**，应能从仓库目录结构或业务域推导出来，而不是随意命名。

### 4.2 推荐取值

| 项目形态 | scope 取值示例 |
| --- | --- |
| 前端业务 | `user` `order` `pay` `cart` `home` `router` `store` `api` `ui` |
| 后端服务 | `auth` `order` `inventory` `gateway` `db` `mq` `job` |
| 组件库 | 组件名小写中划线：`date-picker` `table` `modal` |
| Monorepo | **包名必填**：`@app/web` `@pkg/utils` `@sdk/core` |
| 工程基建 | `deps` `build` `ci` `docker` `nginx` `config` |

### 4.3 书写规则

- 全小写，单词间用中划线 `-`，不用下划线或驼峰；
- 不要写文件路径（`src/views/user/index.vue` 太长，写 `user` 即可）；
- 跨多个模块的改动 → 拆分提交；确实无法拆时，scope 可省略或写上层域（如 `api`）；
- 影响全仓库（如升级 Node 版本、统一代码风格）→ scope 省略或写 `*`（需团队约定）。

---

## 五、description 书写规则

### 5.1 三条核心要求

1. **祈使句 / 动宾结构**：写"这个提交会做什么"，等价于补全 "This commit will ___"；
2. **首字母小写（英文）**、**句末不加句号**；
3. **具体可验证**：能让人不看 diff 就知道改了什么。

### 5.2 对比示例

| 反例 | 问题 | 正例 |
| --- | --- | --- |
| `改bug` | 无信息量 | `fix(order): 修复优惠券叠加导致金额为负` |
| `update code` | 太笼统 | `refactor(api): 统一请求层错误码处理` |
| `修复了用户登录的时候验证码不刷新的问题` | 冗长、口语 | `fix(login): 验证码过期后自动刷新` |
| `feat: 新增功能。` | 句号、无内容 | `feat(user): 支持手机号验证码登录` |
| `fixed bug` | 过去式 | `fix(cart): 修正商品数量上限判断` |
| `feat(pay): Added wechat pay.` | 过去式 + 大写 + 句号 | `feat(pay): add WeChat Pay callback` |

### 5.3 中英文选择

- 中文描述：不加句号，动词开头（"新增/修复/优化/调整/移除/重命名"）；
- 英文描述：全部小写（专有名词如 `WeChat`、`OAuth2` 保留原大小写），动词原形开头（`add` / `fix` / `remove` / `refactor` / `bump`）；
- 同一仓库禁止混用，写进仓库 README。

---

## 六、body 正文写法

body 用于解释 **为什么改**（diff 只能说明"改了什么"），以下场景应当写 body：

- 修复线上问题：写清现象、根因、修复思路；
- 行为变更：写清旧行为 → 新行为；
- 复杂重构：写清拆分逻辑与迁移步骤；
- 性能优化：写清优化前后指标（如首屏 3.2s → 1.8s）；
- 有取舍的技术方案：写清为什么不用另一种方案。

写法要求：

- 与 Header 之间空一行；
- 建议每行不超过 72 字符，便于 `git log` 阅读；
- 可用 `-` 列表罗列要点；
- 不复述 diff（不要写"把 a 改成 b"这类 diff 已表达的内容）。

---

## 七、footer 脚注写法

### 7.1 Issue / 需求关联

| 写法 | 含义 |
| --- | --- |
| `Closes #123` | 合并后自动关闭 Issue 123 |
| `Fixes #123` | 同上，偏向缺陷 |
| `Refs #456` | 仅关联不关闭（分多次提交完成需求时用） |
| `See also #789` | 相关参考 |

### 7.2 其他常见 token

| token | 用途 |
| --- | --- |
| `BREAKING CHANGE: xxx` | 破坏性变更描述（大写，见第八章） |
| `Reviewed-by: 张三` | 评审人（部分团队要求） |
| `Co-authored-by: 李四 <li@x.com>` | 协作提交署名 |
| `Signed-off-by: 王五 <w@x.com>` | DCO 签署（开源项目常用） |

> 注意：`BREAKING CHANGE` 必须全大写且带冒号空格才被工具识别；写成 `breaking change:` 不会触发主版本升级。

---

## 八、破坏性变更（BREAKING CHANGE）

### 8.1 什么算破坏性变更

- 公开 API/接口的请求参数、返回结构、字段名称变更或删除；
- 函数签名、参数顺序、必填性变更；
- 组件 props / 事件名变更；
- 配置文件结构、环境变量名变更；
- 数据库表结构变更且需要手工迁移；
- 组件库删除某个导出、修改默认行为；
- 依赖大版本升级导致下游需要适配。

### 8.2 两种等价写法

写法 A：Header 加 `!`

```
feat!(api): 下单接口改为异步受理

订单创建改为异步排队，返回受理单号而非订单号，客户端需轮询查询结果
```

写法 B：Header 正常 + Footer 声明

```
feat(api): 下单接口改为异步受理

订单创建改为异步排队，返回受理单号而非订单号

BREAKING CHANGE: /api/order/create 不再返回 orderId，改为返回 acceptId，客户端需轮询 /api/order/status
```

> 建议：影响面大、需要迁移说明时用写法 B，必须在脚注里写清 **迁移方式**。

---

## 九、与语义化版本（SemVer）的对应关系

版本号格式：`MAJOR.MINOR.PATCH`（如 `2.4.1`）

| 提交类型 | 版本变化 | 示例 |
| --- | --- | --- |
| 含 `BREAKING CHANGE` / `!` | MAJOR +1，其余归零 | `2.4.1 → 3.0.0` |
| `feat` | MINOR +1，PATCH 归零 | `2.4.1 → 2.5.0` |
| `fix` / `perf` / 其他修复类 | PATCH +1 | `2.4.1 → 2.4.2` |
| 仅 `docs` / `style` / `test` / `chore` | 不发版 | 无 |

配合 `semantic-release` 或 `standard-version` 时，上述映射由工具自动执行，因此 type 的正确性直接决定版本号准确性。

---

## 十、正例与反例大全

### 10.1 正例（可直接照抄）

**基础类**

```
feat(user): 支持手机号验证码登录
feat(order): 新增订单批量导出 Excel
fix(order): 修复优惠券叠加导致实付金额为负
fix(pay): 修复微信回调重复处理导致重复入账
perf(list): 虚拟滚动替代全量渲染，首屏耗时 3.2s → 1.1s
refactor(api): 拆分 request 模块，统一错误码处理
style: 执行 eslint --fix 统一格式
docs(readme): 补充本地启动与调试说明
test(login): 补充验证码过期场景单测
build(deps): 升级 vite 4.5.0 → 5.4.0
ci: 新增 PR 阶段 lint 与单测校验任务
chore: 忽略 .idea 与 .DS_Store
```

**带 body**

```
fix(stock): 修复秒杀超卖

压测发现并发扣减未加行锁，导致库存被扣成负数
- 扣减改为 UPDATE ... WHERE stock > 0 原子操作
- 补充并发单测，覆盖 200 并发场景

Closes #231
```

**破坏性变更**

```
feat!(auth): 移除旧版 token 解析

BREAKING CHANGE: 旧 token 格式不再兼容，所有客户端需升级到 v2 SDK 并重新登录
```

**回滚**

```
revert: feat(pay): 接入微信支付

This reverts commit 3f2a1b0d.
回滚原因：微信侧回调验签异常，待修复后重新合入

Refs #208
```

**机器人/自动化提交**

```
chore(deps): bump axios from 1.6.0 to 1.7.2
```

### 10.2 反例清单（禁止合入）

```
改bug
更新
优化一下
修复问题
提交代码
feat: 新增功能。
Feat(user): 新增登录
fix(user)：修复登录（中文冒号）
fix user 修复登录（缺冒号）
feat(user): 修改 user 模块的 index.vue 第 35 行代码（描述实现细节）
feat: 新增登录/注册/找回密码/个人中心/设置页（一个提交塞五个功能）
update(readme): 更新文档（type 非法，应为 docs）
```

---

## 十一、提交粒度与拆分原则

### 11.1 一个 commit 应满足

- **原子性**：能独立描述、独立回滚，回滚后系统仍可运行；
- **可编译**：提交后主干/分支可正常构建（禁止"提交半成品导致编译失败"）；
- **单一意图**：不混入无关格式化、无关依赖升级、无关日志删除。

### 11.2 常见拆分场景

| 场景 | 拆分方式 |
| --- | --- |
| 新增功能 + 顺手改格式 | 先 `style` 单独提交，再 `feat` |
| 依赖升级 + 适配代码 | `build(deps)` 升级 + `refactor` 适配，分开 |
| 修复 bug + 补测试 | 可合并为一个 `fix`（测试是修复的一部分）；若测试独立性强可拆 `test` |
| 大重构 | 按"纯改名/移动 → 抽公共逻辑 → 替换调用方"逐步拆，每步可运行 |
| 一次 PR 涉及多模块 | 每模块一个 commit，scope 各不相同 |

### 11.3 提交频率

- 本地开发允许小步频繁提交，但 **推送前 / 提 PR 前** 用 `git rebase -i` 整理；
- 整理原则：合并同类、改写不规范 message、删除调试提交（`wip`/`tmp`/`asdf`）；
- 已被他人拉取（共享分支）的提交 **禁止 force push 改写历史**。

---

## 十二、分支命名规范

### 12.1 命名格式

```
<type>/<short-desc>
```

| 前缀 | 用途 | 示例 |
| --- | --- | --- |
| `feature/` | 新需求开发 | `feature/user-sms-login` |
| `bugfix/` | 常规缺陷修复 | `bugfix/order-price-calc` |
| `hotfix/` | 线上紧急修复（从主干/发布分支拉出） | `hotfix/pay-callback-duplicate` |
| `refactor/` | 重构 | `refactor/request-module` |
| `perf/` | 性能专项 | `perf/list-virtual-scroll` |
| `docs/` | 文档专项 | `docs/api-guide` |
| `test/` | 测试专项 | `test/login-e2e` |
| `chore/` | 工程/依赖/CI | `chore/upgrade-vite` |
| `release/` | 发布准备 | `release/v2.4.0` |

### 12.2 规则

- 全小写，单词用中划线连接；
- 关联需求单可追加单号：`feature/user-sms-login-208`；
- 禁止 `dev`、`test`、`zhangsan`、`tmp1` 这类无意义分支名；
- 禁止长期存在的"个人分支"合入主干，必须走 PR。

---

## 十三、工具链落地配置

推荐组合：**commitlint（校验）+ husky（钩子）+ commitizen/cz-git（辅助生成）+ lint-staged（暂存区校验）+ semantic-release（自动发版）**。

### 13.1 安装依赖

```bash
pnpm add -D @commitlint/cli @commitlint/config-conventional husky lint-staged cz-git commitizen
```

### 13.2 `commitlint.config.cjs`

```js
/** @type {import('@commitlint/types').UserConfig} */
module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'type-enum': [
      2,
      'always',
      ['feat', 'fix', 'docs', 'style', 'refactor', 'perf', 'test', 'build', 'ci', 'chore', 'revert'],
    ],
    'type-case': [2, 'always', 'lower-case'],
    'type-empty': [2, 'never'],
    'scope-case': [2, 'always', 'lower-case'],
    'subject-empty': [2, 'never'],
    'subject-full-stop': [2, 'never', '.'],
    'subject-case': [0], // 中文场景关闭大小写限制
    'header-max-length': [2, 'always', 72],
    'body-leading-blank': [2, 'always'],
    'footer-leading-blank': [2, 'always'],
  },
};
```

### 13.3 `cz-git` 交互式提交（写入 `commitlint.config.cjs` 同一文件）

```js
module.exports = {
  // ...上述 rules
  prompt: {
    messages: {
      type: '选择提交类型',
      scope: '输入修改范围（可选）',
      subject: '填写简短描述（必填）',
      body: '填写详细描述（可选）',
      breaking: '列出破坏性变更（可选）',
      footer: '关联 issue（可选，如 #123）',
    },
    types: [
      { value: 'feat', name: 'feat:     新增功能' },
      { value: 'fix', name: 'fix:      修复缺陷' },
      { value: 'docs', name: 'docs:     文档变更' },
      { value: 'style', name: 'style:    代码格式' },
      { value: 'refactor', name: 'refactor: 代码重构' },
      { value: 'perf', name: 'perf:     性能优化' },
      { value: 'test', name: 'test:     测试相关' },
      { value: 'build', name: 'build:    构建/依赖' },
      { value: 'ci', name: 'ci:       CI 配置' },
      { value: 'chore', name: 'chore:    其他杂项' },
      { value: 'revert', name: 'revert:   回滚提交' },
    ],
  },
};
```

`package.json` 中：

```json
{
  "scripts": {
    "commit": "git-cz",
    "prepare": "husky install"
  },
  "config": {
    "commitizen": {
      "path": "node_modules/cz-git"
    }
  }
}
```

### 13.4 husky 钩子

```bash
npx husky add .husky/commit-msg 'npx --no -- commitlint --edit "$1"'
npx husky add .husky/pre-commit 'npx lint-staged'
```

生成文件示例 `.husky/commit-msg`：

```sh
#!/usr/bin/env sh
npx --no -- commitlint --edit "$1"
```

### 13.5 lint-staged（`package.json` 或 `.lintstagedrc`）

```json
{
  "lint-staged": {
    "*.{js,ts,vue,jsx,tsx}": ["eslint --fix", "prettier --write"],
    "*.{css,scss,less}": ["stylelint --fix", "prettier --write"],
    "*.{json,md,yml,yaml}": ["prettier --write"]
  }
}
```

### 13.6 git commit 模板（降低填写门槛）

```bash
git config --local commit.template .gitmessage
```

`.gitmessage`：

```
# <type>(<scope>): <描述>
# type: feat|fix|docs|style|refactor|perf|test|build|ci|chore|revert
# 示例: feat(user): 支持手机号验证码登录
# 描述用祈使句，结尾不加句号，长度 ≤ 72 字符
#
# 详细说明（为什么改、影响面）：
#
# 关联 issue / 破坏性变更：
```

### 13.7 自动发版（可选，`semantic-release`）

`.releaserc.json` 片段：

```json
{
  "branches": ["main", "release/*"],
  "plugins": [
    "@semantic-release/commit-analyzer",
    "@semantic-release/release-notes-generator",
    "@semantic-release/changelog",
    "@semantic-release/npm",
    "@semantic-release/github",
    "@semantic-release/git"
  ]
}
```

> 轻量替代：`npx standard-version` 本地生成 CHANGELOG 与版本号，不上 CI。

### 13.8 CI 兜底校验

CI 中追加（防止绕过本地钩子）：

```yaml
- name: 校验提交信息
  run: npx commitlint --from ${{ github.event.pull_request.base_sha }} --to ${{ github.event.pull_request.head_sha }}
```

### 13.9 存量仓库改造建议

1. 先落 `commitlint` + `commit-msg` 钩子，只约束新提交，不追溯历史；
2. 一个月后加 CI 校验与 CHANGELOG 自动生成；
3. `git log` 历史不规范的仓库，可在 `CONTRIBUTING.md` 中声明"历史不做统一"，避免陷入大规模 rebase。

---

## 十四、Pull Request 与合并规范

### 14.1 PR 要求

- **标题**遵循与 commit 相同的格式：`feat(user): 支持手机号验证码登录`；
- 合并方式：默认 **Squash Merge**，squash 后的 message 用 PR 标题 + 正文生成，保证主干历史一条一功能；
- 主干分支（`main` / `master`）禁止直接 push，必须走 PR + 至少 1 人 approve；
- 合并前必须通过：lint、单测、构建、commitlint；
- PR 描述必须包含：改动点、影响面、自测方式、截图/录屏（UI 改动）、关联单号。

### 14.2 合并策略对照

| 策略 | 适用 | 主干历史 |
| --- | --- | --- |
| Squash Merge（推荐） | 功能分支合主干 | 线性、一功能一提交 |
| Merge Commit | 需要保留分支拓扑的发布合并 | 有 merge 记录 |
| Rebase Merge | 提交本身已规范且需保留粒度 | 线性、多提交 |

### 14.3 发布分支

- 从主干切 `release/vX.Y.Z`，仅接收 `fix` / 紧急修复；
- 发布完成打 tag：`v2.4.0`（与 SemVer 一致）；
- `hotfix/` 分支需同时合入主干与发布分支，避免下次发布丢失修复。

---

## 十五、常见问题 FAQ

**Q1：一次提交改动跨多个模块，scope 怎么写？**
优先拆分。确实无法拆（如全局接口契约变更）时，scope 可省略，或写上层域（`api`、`global`），并在 body 中说明涉及哪些模块。

**Q2：紧急线上修复还要走规范吗？**
要。`hotfix(pay): 修复回调重复入账` 比 `紧急修复` 更有价值，且事后排查依赖它。可以先 commit 上线，但 message 必须合规。

**Q3：本地提交被 commitlint 拦截，如何临时跳过？**
`git commit --no-verify`（跳过钩子）。仅限本地临时提交，PR 会被 CI 二次拦截，不要作为习惯。

**Q4：回滚怎么写？**
用 `git revert`，自动生成 `revert: <被回滚的 header>`，再在 body 补回滚原因。禁止用 `reset` 强推共享分支。

**Q5：merge commit 会被 commitlint 报错吗？**
默认 `@commitlint/config-conventional` 会放行 `Merge branch ...` 这类默认格式。若被拦截，检查是否自定义了 `type-enum` 且未排除 merge。

**Q6：中文描述能通过 subject-case 校验吗？**
中文字符没有大小写概念，通常不会触发；但 `subject-full-stop` 会对中文句号"。"报错，这正是我们要的效果（禁止结尾句号）。

**Q7：依赖升级用 `build` 还是 `chore`？**
升级运行时依赖、构建工具版本用 `build(deps)`；改了 `.gitignore`、删了无用文件用 `chore`。

**Q8：一个需求反复修改，产生了 20 个 commit 怎么办？**
提 PR 前 `git rebase -i HEAD~20` 合并为 1–3 个语义清晰的提交；若已推送他人分支，改为在 PR 里 squash merge。

**Q9：规范会不会太重、影响效率？**
日常只需要 `type(scope): 描述` 一行；body/footer 只在复杂改动时使用。配合 `git cz` 交互式生成，实际输入成本低于手写。

**Q10：老仓库历史不规范，需要重写吗？**
不需要。规范只约束向前的新提交；重写历史风险高、收益低。

---

## 十六、附录

### 16.1 一页速查表

| 我要做的事 | 提交写法 |
| --- | --- |
| 加功能 | `feat(模块): 做什么` |
| 修 bug | `fix(模块): 修了什么` |
| 改文档 | `docs: 改了什么` |
| 只改格式 | `style: 说明` |
| 重构 | `refactor(模块): 怎么重构` |
| 提性能 | `perf(模块): 优化点 + 指标` |
| 加测试 | `test(模块): 覆盖了什么场景` |
| 升级依赖 | `build(deps): 升级 x 从 a 到 b` |
| 改流水线 | `ci: 做了什么` |
| 杂项 | `chore: 说明` |
| 回滚 | `revert: <原 header>` |
| 破坏性变更 | 类型后加 `!` + `BREAKING CHANGE:` |

### 16.2 提交前自查清单

- [ ] type 在枚举内，小写
- [ ] 有 scope（能写就写），全小写中划线
- [ ] 描述是祈使句、具体、无句号、≤ 72 字符（中文 ≤ 50 字）
- [ ] 冒号是英文半角，冒号后有一个空格
- [ ] 一个提交只做一件事，包含的改动都能用这句话概括
- [ ] 提交后代码可编译、可运行
- [ ] 破坏性变更已用 `!` 或 `BREAKING CHANGE:` 标注，并写了迁移方式
- [ ] 关联了 Issue（`Closes #xxx`）
- [ ] 没有残留调试代码、注释掉的代码、临时 `console.log`

### 16.3 评审人检查清单

- [ ] commit message 符合规范，能从 message 判断改动性质
- [ ] 未混入无关改动（格式化、依赖、日志）
- [ ] PR 标题与 squash 后 message 一致且合规
- [ ] 破坏性变更已在 PR 描述中说明影响面与升级指引
- [ ] 涉及数据库/接口/配置变更已同步相关方

### 16.4 参考资源

- Conventional Commits 规范：https://www.conventionalcommits.org
- Angular 提交约定：https://github.com/angular/angular/blob/main/CONTRIBUTING.md
- commitlint 文档：https://commitlint.js.org
- cz-git 文档：https://cz-git.qbb.sh
- Semantic Versioning：https://semver.org/lang/zh-CN/

### 16.5 变更记录

| 版本 | 日期 | 变更内容 | 维护人 |
| --- | --- | --- | --- |
| v1.0 | 2026-11-22 | 首次发布，确立 type 枚举、格式约束与工具链方案 | 研发组 |

> （注：部分内容由豆包 AI 生成）