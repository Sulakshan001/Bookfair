// src/services/Admin.js
import axios from "axios";

export default class Admin {
  static BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8088";

  /* ================= COMMON HEADER ================= */
  static getHeader() {
    const token =
      localStorage.getItem("token") ||
      localStorage.getItem("accessToken") ||
      sessionStorage.getItem("token");

    return {
      headers: {
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        "Content-Type": "application/json",
      },
    };
  }

  /* ================= RESPONSE UNWRAP =================
     Your backend returns: ApiResponse.ok(msg, data, status)
     So axios returns: { message, data, statusCode }
     We want the actual data.
  */
  static unwrap(response) {
    return response?.data?.data ?? response?.data;
  }

  /* ================= DASHBOARD ================= */
  // ✅ Exists in backend: GET /api/admin/dashboard/statistics
  static async getDashboardStats() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/dashboard/statistics`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getDashboardStats");
      throw error;
    }
  }

  // ✅ Exists in backend: GET /api/admin/dashboard/metrics
  static async getSystemMetrics() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/dashboard/metrics`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getSystemMetrics");
      throw error;
    }
  }

  /* ================= USER MANAGEMENT ================= */
  // ✅ Exists: GET /api/admin/users?page=0&size=10
  // NOTE: your backend currently ignores "sort" unless you update controller to accept it.
  static async getAllUsers(params = {}) {
    const config = {
      ...this.getHeader(),
      params: {
        page: params.page ?? 0,
        size: params.size ?? 10,
        // backend currently does NOT accept sort param in your pasted controller
        // keep it here only if you upgrade controller to support sort
        ...(params.sort ? { sort: params.sort } : {}),
      },
    };

    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/users`,
        config
      );
      return this.unwrap(response); // Page<UserResponse>
    } catch (error) {
      this.logError(error, "getAllUsers");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/users/{userId}
  static async getUserById(userId) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/users/${userId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getUserById");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/users/role/{role}
  static async getUsersByRole(role) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/users/role/${role}`,
        this.getHeader()
      );
      return this.unwrap(response); // List<UserResponse>
    } catch (error) {
      this.logError(error, "getUsersByRole");
      throw error;
    }
  }

  // ✅ Exists: PUT /api/admin/users/{userId}/role/{newRole}
  static async updateUserRole(userId, newRole) {
    try {
      const response = await axios.put(
        `${this.BASE_URL}/api/admin/users/${userId}/role/${newRole}`,
        {}, // no body needed because role is in URL
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "updateUserRole");
      throw error;
    }
  }

  // ✅ Exists: DELETE /api/admin/users/{userId}
  static async deleteUser(userId) {
    try {
      const response = await axios.delete(
        `${this.BASE_URL}/api/admin/users/${userId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "deleteUser");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/users/count
  static async getTotalUsersCount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/users/count`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getTotalUsersCount");
      throw error;
    }
  }

  /* ================= STALL MANAGEMENT ================= */
  // ✅ Exists: GET /api/admin/stalls
  static async getAllStalls() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/stalls`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getAllStalls");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/stalls/status/{status}
  static async getStallsByStatus(status) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/stalls/status/${status}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getStallsByStatus");
      throw error;
    }
  }

  // ✅ Exists: PUT /api/admin/stalls/{stallId}/status/{status}
  static async updateStallStatus(stallId, status) {
    try {
      const response = await axios.put(
        `${this.BASE_URL}/api/admin/stalls/${stallId}/status/${status}`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "updateStallStatus");
      throw error;
    }
  }

  // ✅ Exists: DELETE /api/admin/stalls/{stallId}
  static async deleteStall(stallId) {
    try {
      const response = await axios.delete(
        `${this.BASE_URL}/api/admin/stalls/${stallId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "deleteStall");
      throw error;
    }
  }

  /* ================= RESERVATIONS ================= */
  // ✅ Exists: GET /api/admin/reservations?page=0&size=10
  static async getAllReservations(params = {}) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/reservations`,
        {
          ...this.getHeader(),
          params: {
            page: params.page ?? 0,
            size: params.size ?? 10,
          },
        }
      );
      return this.unwrap(response); // Page<ReservationResponse>
    } catch (error) {
      this.logError(error, "getAllReservations");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/reservations/{reservationId}
  static async getReservationById(reservationId) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/reservations/${reservationId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getReservationById");
      throw error;
    }
  }

  // ✅ Exists: DELETE /api/admin/reservations/{reservationId}
  static async cancelReservation(reservationId) {
    try {
      const response = await axios.delete(
        `${this.BASE_URL}/api/admin/reservations/${reservationId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "cancelReservation");
      throw error;
    }
  }

  // ✅ Exists: POST /api/admin/reservations/{reservationId}/send-confirmation-email
  static async sendReservationConfirmationEmail(reservationId) {
    try {
      const response = await axios.post(
        `${this.BASE_URL}/api/admin/reservations/${reservationId}/send-confirmation-email`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "sendReservationConfirmationEmail");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/reservations/count
  static async getTotalReservationsCount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/reservations/count`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getTotalReservationsCount");
      throw error;
    }
  }

  // ✅ You MUST add backend endpoint for this:
  // GET /api/admin/reservations/pending
  static async getPendingReservations() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/reservations/pending`,
        this.getHeader()
      );
      return this.unwrap(response); // List<ReservationResponse>
    } catch (error) {
      this.logError(error, "getPendingReservations");
      throw error;
    }
  }

  /* ================= PAYMENTS ================= */
  // ✅ Exists: GET /api/admin/payments?page=0&size=10
  static async getAllPayments(params = {}) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/payments`,
        {
          ...this.getHeader(),
          params: {
            page: params.page ?? 0,
            size: params.size ?? 10,
          },
        }
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getAllPayments");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/payments/{paymentId}
  static async getPaymentById(paymentId) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/payments/${paymentId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getPaymentById");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/payments/status/{status}
  static async getPaymentsByStatus(status) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/payments/status/${status}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getPaymentsByStatus");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/payments/total-amount
  static async getTotalPaymentsAmount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/payments/total-amount`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getTotalPaymentsAmount");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/payments/count/successful
  static async getSuccessfulPaymentsCount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/payments/count/successful`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getSuccessfulPaymentsCount");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/payments/count/pending
  static async getPendingPaymentsCount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/payments/count/pending`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getPendingPaymentsCount");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/payments/count/failed
  static async getFailedPaymentsCount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/payments/count/failed`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getFailedPaymentsCount");
      throw error;
    }
  }

  // ✅ Exists: PUT /api/admin/payments/{paymentId}/status/{status}
  static async updatePaymentStatus(paymentId, status) {
    try {
      const response = await axios.put(
        `${this.BASE_URL}/api/admin/payments/${paymentId}/status/${status}`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "updatePaymentStatus");
      throw error;
    }
  }

  // ✅ Exists: PUT /api/admin/payments/{paymentId}/confirm
  static async confirmPayment(paymentId) {
    try {
      const response = await axios.put(
        `${this.BASE_URL}/api/admin/payments/${paymentId}/confirm`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "confirmPayment");
      throw error;
    }
  }

  // ✅ Exists: POST /api/admin/payments/{paymentId}/send-confirmation-email
  static async sendPaymentConfirmationEmail(paymentId) {
    try {
      const response = await axios.post(
        `${this.BASE_URL}/api/admin/payments/${paymentId}/send-confirmation-email`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "sendPaymentConfirmationEmail");
      throw error;
    }
  }

  /* ================= EMAIL NOTIFICATIONS ================= */
  // ✅ Exists: GET /api/admin/emails?page=0&size=10
  static async getAllEmailNotifications(params = {}) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/emails`,
        {
          ...this.getHeader(),
          params: {
            page: params.page ?? 0,
            size: params.size ?? 10,
          },
        }
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getAllEmailNotifications");
      throw error;
    }
  }

  // ✅ Exists: POST /api/admin/emails/{id}/resend
  static async resendEmailNotification(emailNotificationId) {
    try {
      const response = await axios.post(
        `${this.BASE_URL}/api/admin/emails/${emailNotificationId}/resend`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "resendEmailNotification");
      throw error;
    }
  }

  // ✅ Exists: GET /api/admin/emails/failed
  static async getFailedEmailNotifications() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/emails/failed`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getFailedEmailNotifications");
      throw error;
    }
  }

  /* ================= QR PASS MANAGEMENT ================= */
  // ✅ GET /api/admin/qr-passes - Get all QR passes with pagination
  static async getAllQrPasses(page = 0, size = 10, sortBy = "qrId", direction = "desc") {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/qr-passes`,
        {
          ...this.getHeader(),
          params: { page, size, sortBy, direction },
        }
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getAllQrPasses");
      throw error;
    }
  }

  // ✅ GET /api/admin/qr-passes/{qrId} - Get QR pass by ID
  static async getQrPassById(qrId) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/qr-passes/${qrId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getQrPassById");
      throw error;
    }
  }

  // ✅ GET /api/admin/qr-passes/by-reservation/{reservationId}
  static async getQrPassByReservationId(reservationId) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/qr-passes/by-reservation/${reservationId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getQrPassByReservationId");
      throw error;
    }
  }

  // ✅ GET /api/admin/qr-passes/by-code/{qrCode}
  static async getQrPassByQrCode(qrCode) {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/qr-passes/by-code/${qrCode}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getQrPassByQrCode");
      throw error;
    }
  }

  // ✅ PUT /api/admin/qr-passes/{qrId}/activate
  static async activateQrPass(qrId) {
    try {
      const response = await axios.put(
        `${this.BASE_URL}/api/admin/qr-passes/${qrId}/activate`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "activateQrPass");
      throw error;
    }
  }

  // ✅ PUT /api/admin/qr-passes/{qrId}/deactivate
  static async deactivateQrPass(qrId) {
    try {
      const response = await axios.put(
        `${this.BASE_URL}/api/admin/qr-passes/${qrId}/deactivate`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "deactivateQrPass");
      throw error;
    }
  }

  // ✅ PUT /api/admin/qr-passes/{qrId}/mark-used
  static async markQrPassAsUsed(qrId) {
    try {
      const response = await axios.put(
        `${this.BASE_URL}/api/admin/qr-passes/${qrId}/mark-used`,
        {},
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "markQrPassAsUsed");
      throw error;
    }
  }

  // ✅ DELETE /api/admin/qr-passes/{qrId}
  static async deleteQrPass(qrId) {
    try {
      const response = await axios.delete(
        `${this.BASE_URL}/api/admin/qr-passes/${qrId}`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "deleteQrPass");
      throw error;
    }
  }

  // ✅ GET /api/admin/qr-passes/count/total
  static async getTotalQrPassesCount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/qr-passes/count/total`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getTotalQrPassesCount");
      throw error;
    }
  }

  // ✅ GET /api/admin/qr-passes/count/active
  static async getActiveQrPassesCount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/qr-passes/count/active`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getActiveQrPassesCount");
      throw error;
    }
  }

  // ✅ GET /api/admin/qr-passes/count/inactive
  static async getInactiveQrPassesCount() {
    try {
      const response = await axios.get(
        `${this.BASE_URL}/api/admin/qr-passes/count/inactive`,
        this.getHeader()
      );
      return this.unwrap(response);
    } catch (error) {
      this.logError(error, "getInactiveQrPassesCount");
      throw error;
    }
  }

  /* ================= UTIL ================= */
  static logError(error, label) {
    const status = error?.response?.status;
    const url = error?.config?.url;
    const data = error?.response?.data;
    
    let errorMsg = `[${label}] Status: ${status}`;
    if (status === 403) {
      errorMsg += " - ACCESS DENIED: Check user role/permissions for QR Pass management";
    } else if (status === 401) {
      errorMsg += " - UNAUTHORIZED: Token may be expired or invalid";
    } else if (status === 404) {
      errorMsg += " - NOT FOUND: Resource does not exist on server";
    } else if (status === 500) {
      errorMsg += " - SERVER ERROR: Contact backend support";
    }
    
    console.error(errorMsg, { status, url, data });
  }
}
