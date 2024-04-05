# springboot-mall

### 架構

使用 MVC 模式, Controller-Service-Dao 三層式架構

### 資料夾結構

1. model: 定義 tb 對應到的 java class
2. rowmapper: implement rowMapper, 將 db query 出來的數據轉換為 java object
3. constant: 用來存放常數的 package
4. dto: Data Transfer Obj, 放雜項的 package

### 專案內容

1. 商品功能

   1. 新增修改刪除商品
   2. 查詢商品列表, 包含使用排序、分頁的功能
   3. 單元測試
2. 帳號功能

   1. 註冊帳號
   2. 登入
   3. 單元測試
3. 訂單功能

   1. 創建訂單
   2. 查詢訂單
   3. 單元測試

### 為來擴展

1. 商品功能
   1. 優化查詢列表效率: 使用 elastic search
   2. 權限管理 RBAC: 不是所有人都可以更新刪除 product
   3. Price 使用 Decimal 類型儲存, 非 INT
2. 帳號功能
   1. 每個 api 的 token 驗證
3. 訂單功能
   1. 如何解決搶票問題?多人搶訂單怎麼解決
   2. 狀態問題, 如何處理訂單物流狀態? 訂單退款
   3. 怎麼串接金流
