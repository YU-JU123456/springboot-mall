# springboot-電商網站實作

### 架構

使用 MVC 模式, Controller-Service-Dao 三層式架構

### 資料夾結構

1. model: 定義 tb 對應到的 java class
2. rowmapper: implement rowMapper, 將 db query 出來的數據轉換為 java object
3. constant: 用來存放常數的 package
4. dto: Data Transfer Obj, 放雜項的 package
5. MySQL.txt: 建立所有 table 的 SQL 語法

### 專案內容

1. 商品功能

   1. 新增修改刪除商品
   2. 查詢商品列表, 包含使用排序、分頁的功能
   3. 單元測試
2. 帳號功能

   1. 註冊帳號
      1. /users/register 為註冊 user 權限帳號的 api, 不檢查 Authentication
      2. /admin/register 為註冊 admin/user 權限帳號的 api, 僅限 admin 權限使用者使用, 非 admin 權限使用者會回傳 403 錯誤
      3. 註冊成功後, 回傳 userId 及權限名稱
      4. 若帳號已被註冊, 回傳 411 錯誤
   2. 登入
      1. 實作自定義 response code
         * 如果該帳號不存在, 回傳 AUTHENTICATION_NOT_EXIST 錯誤
         * 其餘為 401
      2. DB 有預先建立一組 admin 權限的帳號: admin/admin (目前為手動新增)
   3. 單元測試
3. 訂單功能

   1. 創建訂單
   2. 查詢訂單
   3. 單元測試
4. Spring Security

   1. 身份認證
      1. 除了 /users/register 外, 其餘 api 皆會檢查身分認證
         * login 時將帳密資訊存在 header 而不是 request body
         * 其餘認證檢查 cookie 中的 session
      2. 使用 Hash 中的 BCrypt 演算法在 DB 中儲存密碼, 取代明碼儲存
   2. 使用 RBAC 權限管理
      1. 只有 ADMIN 權限使用者可以新增、修改、刪除商品
   3. CORS 安全機制
      1. 由後端告訴前端信任來源
   4. CSRF 檢查
      1. 除了 /users/login 外, 其餘 api 皆會檢查 request header 裡是否有帶 X-XSRF-TOKEN
   5. 單元測試

### 未來擴展

1. Data 前處理

   1. Initialize database: 初始化所有 tables
   2. 預先建立一組 admin 權限帳號
2. 商品功能

   1. 優化查詢列表效率: 使用 elastic search
   2. Price 使用 Decimal 類型儲存, 非 INT
3. 帳號功能

   1. 每個 api 的 token 驗證
4. 訂單功能

   1. 如何解決搶票問題?多人搶訂單怎麼解決
   2. 狀態問題, 如何處理訂單物流狀態? 訂單退款
   3. 怎麼串接金流
