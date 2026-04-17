# Subscription List

---

## 目錄

- [1. 產品概覽](#1-產品概覽)
- [2. 目標使用者與情境](#2-目標使用者與情境)
- [3. 功能需求](#3-功能需求)
  - [3.1 訂閱列表（首頁）](#31-訂閱列表首頁)
  - [3.2 新增 / 編輯訂閱](#32-新增--編輯訂閱)
  - [3.3 幣別換算](#33-幣別換算)
  - [3.4 每月平均費用計算](#34-每月平均費用計算)
- [4. 非功能需求](#4-非功能需求)
- [5. 資料模型](#5-資料模型)
- [6. 畫面流程](#6-畫面流程)
- [7. 畫面設計規格](#7-畫面設計規格)
  - [7.1 列表畫面](#71-列表畫面)
  - [7.2 新增 / 編輯畫面](#72-新增--編輯畫面)
- [8. 技術架構](#8-技術架構)
- [9. 模組結構](#9-模組結構)
- [10. 驗收條件](#10-驗收條件)

---

## 1. 產品概覽

**Subscription List** 是一款個人訂閱管理 App，幫助使用者記錄、追蹤所有訂閱服務的費用與扣款時程，並自動換算為新台幣（TWD）以呈現每月平均支出。

---

## 2. 目標使用者與情境

| 項目 | 說明 |
|---|---|
| **使用者** | 個人用戶，管理自身訂閱服務 |
| **主要情境** | 新增訂閱、查看每月總支出、確認下次扣款日 |
| **裝置** | Android 手機（Min SDK 30） |
| **網路依賴** | 幣別換算需要網路；其餘功能離線可用 |

---

## 3. 功能需求

### 3.1 訂閱列表（首頁）

- 顯示所有已建立的訂閱項目
- 每個列表項目顯示：
  - 服務名稱
  - 方案名稱
  - 原始費用（原始幣別）
  - 每月平均費用（TWD）
  - 下次扣款日期
- 頂部顯示**每月預估總支出**（所有訂閱每月平均費用加總，單位 TWD）
- 支援**排序**（使用者可切換）：
  - 依服務名稱（A → Z）
  - 依每月費用（高 → 低）
  - 依下次扣款日期（最近優先）
- 支援**新增**按鈕（FAB）進入新增畫面
- 支援**滑動刪除**或長按進入刪除確認

### 3.2 新增 / 編輯訂閱

使用者可建立或修改一筆訂閱，需填寫：

| 欄位 | 類型 | 說明 | 必填 |
|---|---|---|---|
| 服務名稱 | 文字 | 如 Netflix、Spotify | ✅ |
| 方案名稱 | 文字 | 如「標準 4K 方案」 | ✅ |
| 價格 | 數字（小數點） | 原始計費金額 | ✅ |
| 幣別 | 下拉選單 | 支援常見幣別，見 §3.3 | ✅ |
| 計費週期 | 數字（月） | 輸入月數，如 1=每月、12=每年 | ✅ |
| 下次扣款日期 | 日期選擇器 | 下一次實際扣款日 | ✅ |

**計算欄位（唯讀，自動顯示）：**
- 每月平均費用（TWD）= 價格 × 匯率 ÷ 計費週期月數

### 3.3 幣別換算

- **基準幣別**：固定為 TWD（新台幣）
- **支援幣別**：USD、EUR、TWD
- **匯率來源**：即時 API（如 [ExchangeRate-API](https://www.exchangerate-api.com/) 免費版）
- **快取策略**：匯率每 24 小時更新一次，無網路時使用最後快取值
- **顯示規則**：
  - 原始費用保留原始幣別與金額
  - 換算後一律顯示 TWD，取整數（四捨五入）
  - 若匯率尚未載入或失敗，顯示「--」並提示

### 3.4 每月平均費用計算

```
每月平均費用（TWD）= 原始價格 × 匯率（→ TWD）÷ 計費週期月數
```

範例：

| 服務 | 原始費用 | 計費週期 | 匯率 | 每月平均（TWD） |
|---|---|---|---|---|
| Netflix | USD 22.99 | 1 月 | 32.5 | ≈ NT$747 |
| iCloud | USD 32.99 | 12 月 | 32.5 | ≈ NT$89 |
| YouTube Premium | TWD 179 | 1 月 | 1 | NT$179 |

---

## 4. 非功能需求

| 項目 | 規格 |
|---|---|
| **離線可用** | 除幣別換算外，所有功能離線可用 |
| **資料儲存** | 本機 Room 資料庫，無雲端同步 |
| **推播通知** | 不支援（不在 v1.0 範圍） |
| **多帳號** | 不支援 |
| **統計圖表** | 不支援 |
| **分類標籤** | 不支援 |

---

## 5. 資料模型

### Domain Entity

```kotlin
data class Subscription(
    val id: String,                   // UUID
    val serviceName: String,
    val planName: String,
    val price: Double,                // 原始金額
    val currency: String,             // ISO 4217，如 "USD"
    val billingCycleMonths: Int,      // 計費週期（月數）
    val nextBillingDate: LocalDate,   // 下次扣款日
)
```

### Calculated / Derived

```kotlin
data class SubscriptionWithCost(
    val subscription: Subscription,
    val monthlyAverageTwd: Double?,   // null = 匯率未載入
)
```

### Exchange Rate Cache

```kotlin
data class ExchangeRate(
    val baseCurrency: String,         // "TWD"
    val rates: Map<String, Double>,   // "USD" → 32.5
    val updatedAt: Instant,
)
```

---

## 6. 畫面流程

```
App 啟動
   └─▶ 訂閱列表畫面（首頁）
         ├─▶ [FAB 新增] ──▶ 新增訂閱畫面 ──▶ 儲存 ──▶ 返回列表
         ├─▶ [點擊項目] ──▶ 編輯訂閱畫面 ──▶ 儲存 ──▶ 返回列表
         ├─▶ [滑動刪除] ──▶ 確認 Dialog ──▶ 刪除
         └─▶ [排序選單] ──▶ 切換排序方式
```

---

## 7. 畫面設計規格

### 7.1 列表畫面

```
┌──────────────────────────────────────┐
│  Subscription List          [排序 ▼] │
├──────────────────────────────────────┤
│  每月預估總支出                        │
│  NT$ 2,340 / 月                       │
├──────────────────────────────────────┤
│  ┌────────────────────────────────┐  │
│  │ Netflix                        │  │
│  │ 標準 4K 方案       USD 22.99   │  │
│  │ 下次扣款：2026/05/01  NT$747/月 │  │
│  └────────────────────────────────┘  │
│  ┌────────────────────────────────┐  │
│  │ iCloud+                        │  │
│  │ 200GB 方案         USD 32.99/年│  │
│  │ 下次扣款：2026/06/15   NT$89/月 │  │
│  └────────────────────────────────┘  │
│                                      │
│                         [  +  FAB ]  │
└──────────────────────────────────────┘
```

### 7.2 新增 / 編輯畫面

```
┌──────────────────────────────────────┐
│  ← 新增訂閱                    [儲存] │
├──────────────────────────────────────┤
│  服務名稱                             │
│  ┌────────────────────────────────┐  │
│  │ Netflix                        │  │
│  └────────────────────────────────┘  │
│  方案名稱                             │
│  ┌────────────────────────────────┐  │
│  │ 標準 4K 方案                   │  │
│  └────────────────────────────────┘  │
│  價格                   幣別          │
│  ┌───────────────┐  ┌─────────────┐  │
│  │ 22.99         │  │ USD       ▼ │  │
│  └───────────────┘  └─────────────┘  │
│  計費週期（月）                        │
│  ┌────────────────────────────────┐  │
│  │ 1                              │  │
│  └────────────────────────────────┘  │
│  下次扣款日期                          │
│  ┌────────────────────────────────┐  │
│  │ 2026/05/01               [📅]  │  │
│  └────────────────────────────────┘  │
│                                      │
│  每月平均費用（TWD）                   │
│  ≈ NT$ 747 / 月                       │
└──────────────────────────────────────┘
```

---

## 8. 技術架構

採用 **Clean Architecture + MVVM + UDF**，多模組從第一天起。

### 依賴關係

```
Presentation → Domain ← Data
```

| 層 | 職責 |
|---|---|
| **Domain** | `Subscription` entity、`SubscriptionRepository` interface、`ExchangeRateRepository` interface、UseCases |
| **Data** | Room DAO、Retrofit API、Repository 實作、DTO Mapper |
| **Presentation** | ViewModel、UiState/UiEvent、Compose Screen |

### UseCase 清單

| UseCase | 說明 |
|---|---|
| `GetSubscriptionsUseCase` | 取得訂閱列表（Flow） |
| `AddSubscriptionUseCase` | 新增訂閱 |
| `UpdateSubscriptionUseCase` | 修改訂閱 |
| `DeleteSubscriptionUseCase` | 刪除訂閱 |
| `GetExchangeRatesUseCase` | 取得匯率（含快取） |
| `CalculateMonthlyAverageUseCase` | 計算每月平均費用（TWD） |

---

## 9. 模組結構

```
:app
├── :feature:subscription
│   ├── domain/
│   │   ├── model/Subscription.kt
│   │   ├── repository/SubscriptionRepository.kt
│   │   └── usecase/（各 UseCase）
│   ├── data/
│   │   ├── local/SubscriptionDao.kt
│   │   ├── local/SubscriptionEntity.kt
│   │   ├── mapper/SubscriptionMapper.kt
│   │   ├── repository/SubscriptionRepositoryImpl.kt
│   │   └── di/SubscriptionDataModule.kt
│   └── presentation/
│       ├── list/SubscriptionListViewModel.kt
│       ├── list/SubscriptionListUiState.kt
│       ├── list/SubscriptionListUiEvent.kt
│       ├── list/screen/SubscriptionListScreen.kt
│       ├── form/SubscriptionFormViewModel.kt
│       └── form/screen/SubscriptionFormScreen.kt
├── :feature:exchange-rate
│   ├── domain/
│   │   ├── model/ExchangeRate.kt
│   │   ├── repository/ExchangeRateRepository.kt
│   │   └── usecase/GetExchangeRatesUseCase.kt
│   ├── data/
│   │   ├── remote/ExchangeRateApi.kt
│   │   ├── remote/ExchangeRateDto.kt
│   │   ├── local/ExchangeRateDao.kt（快取）
│   │   ├── mapper/ExchangeRateMapper.kt
│   │   ├── repository/ExchangeRateRepositoryImpl.kt
│   │   └── di/ExchangeRateDataModule.kt
│   └── （無獨立 presentation，由 :feature:subscription 的 ViewModel 消費）
:core:common
:core:data（Room Database Builder、OkHttp、共用 Interceptor）
:core:ui（Theme、共用 Composable）
```

---

## 10. 驗收條件

### 訂閱列表
- [ ] 首次開啟顯示空狀態畫面
- [ ] 新增後列表即時更新
- [ ] 每月預估總支出正確加總所有訂閱的每月平均費用（TWD）
- [ ] 排序正常切換

### 新增 / 編輯
- [ ] 所有必填欄位未填時，儲存按鈕禁用或顯示錯誤
- [ ] 計費週期輸入 0 或負數時顯示錯誤
- [ ] 每月平均費用即時反應輸入變動
- [ ] 儲存後返回列表並顯示新項目

### 幣別換算
- [ ] 選擇 TWD 時換算比例為 1（不呼叫 API）
- [ ] 有網路時顯示即時匯率換算後的 TWD 金額
- [ ] 無網路且無快取時顯示「--」
- [ ] 匯率快取超過 24 小時後自動更新

### 刪除
- [ ] 確認後項目從列表消失
- [ ] 每月總支出重新計算
