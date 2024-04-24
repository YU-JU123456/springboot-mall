# springboot-電商網站實作

#### 前言

該專案為自學的課程，搭配改寫及新增功能後呈現的專案, 歡迎討論不吝赐教

目前正在持續更新及優化

---

### 架構

使用 MVC 模式, Controller-Service-Dao 三層式架構

---

### /src/main/java/com.ruby.mall 下的資料夾結構

1. controller: 負責接收 Http request 驗證請求參數
2. server: 負責業務邏輯
3. dao: 與資料庫溝通
4. model: 定義 tb 對應到的 java class
5. rowmapper: implement rowMapper, 將 db query 出來的數據轉換為 java object
6. constant: 用來存放常數的 package
   1. 商品分類類別
   2. 自訂義回傳的錯誤代碼類別
7. dto: Data Transfer Obj, 放雜項的 package
8. security: 放 spring security 相關設定
9. exception: 自訂義的例外類別

---

### 專案內容

1. 商品功能

   1. 新增修改刪除商品
      1. 只有 admin 權限使用者可以使用
   2. 查詢商品列表, 包含使用排序、分頁的功能
   3. 單元測試
      1. 查詢/修改/刪除 不存在的商品
      2. 建立/修改 不完整資訊的商品
      3. 使用 user 權限新增修改刪除商品
      4. 使用錯誤帳密操作
      5. 使用排序、分頁查詢商品
2. 帳號功能

   1. 註冊帳號
      1. /users/register 為註冊 user 權限帳號的 api, 不檢查 Authentication
      2. /admin/register 為註冊 admin/user 權限帳號的 api, 僅限 admin 權限使用者使用, 非 admin 權限使用者會回傳 403 錯誤
      3. 註冊成功後, 回傳 userId 及權限名稱
      4. 若帳號已被註冊, 回傳 response status 400 錯誤, 並回傳自定義錯誤代碼
   2. 登入
      1. 如果該帳號不存在, 回傳 response status 401, 並回傳自定義錯誤代碼
      2. DB 有預先建立一組 admin 權限的帳號: admin/admin (目前為手動新增)
   3. 單元測試
      1. 檢查 db 密碼不為明碼
      2. 用錯誤 email 格式註冊
      3. 使用非法權限名稱註冊
      4. 用已存在的帳號註冊
      5. 使用 user 權限註冊 admin 帳號
      6. 使用錯誤帳密註冊 admin 帳號
      7. 使用不存在的帳號登入
3. 訂單功能

   1. 創建訂單
      1. 只有本人能創建自己的訂單, 非本人創建訂單回傳 response status 401, 並回傳自定義錯誤代碼
      2. 創建完成後, 更新商品庫存數量
   2. 查詢訂單
   3. 單元測試
      1. 建立沒有任何商品的訂單
      2. 建立不存在的使用者訂單
      3. 建立商品不存在的訂單
      4. 建立庫存數量不足的訂單
      5. 建立登入身分和訂單身分不吻合的訂單(即用合法的他人身分下單)
      6. 依分頁取得訂單
      7. 取得空白訂單
      8. 取得使用者不存在的訂單
4. Spring Security

   1. 身份認證

      1. 除了 /users/register 外, 其餘 api 皆會檢查身分認證
         * login 時將帳密資訊存在 header 而不是 request body
         * 其餘認證檢查 cookie 中的 session
      2. 使用 Hash 中的 BCrypt 演算法在 DB 中儲存密碼, 取代明碼儲存
   2. 使用 RBAC 權限管理
   3. CORS 安全機制: 由後端告訴前端信任來源
   4. CSRF 檢查: 除了 /users/login 外, 其餘 api 皆會檢查 request header 裡是否有帶 X-XSRF-TOKEN
   5. 自定義 response body

      1. 發生錯誤時, 除了調整 response code 外, response body 會帶自訂義錯誤代碼及錯誤原因
   6. 單元測試

---

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
