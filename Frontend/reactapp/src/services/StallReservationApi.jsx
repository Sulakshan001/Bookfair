import axios from "axios";


const BASE_URL = "http://localhost:8088"; // change if needed

export const api = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});


const unwrap = (res) => res.data; // full ApiResponse
const dataOnly = (res) => unwrap(res).data; // only "data"

/** ===================== STALL API ===================== **/
export const StallApi = {
  getAll: async () => dataOnly(await api.get("/api/stalls")),
  getAvailable: async () => dataOnly(await api.get("/api/stalls/available")),
  getById: async (stallId) => dataOnly(await api.get(`/api/stalls/${stallId}`)),
  getByCode: async (stallCode) =>
    dataOnly(await api.get(`/api/stalls/code/${stallCode}`)),
};


export const ReservationApi = {
  create: async ({ userId, stallIds }) =>
    dataOnly(await api.post("/api/reservations", { userId, stallIds })),

  getById: async (reservationId) =>
    dataOnly(await api.get(`/api/reservations/${reservationId}`)),

  cancel: async (reservationId) =>
    dataOnly(await api.delete(`/api/reservations/${reservationId}`)),
};

/** ===================== PAYMENT API =====================

 */


export const PaymentApi = {
  create: async ({ reservationId, amount, paymentMethod, paymentDetails }) =>
    dataOnly(
      await api.post("/api/payments", {
        reservationId,
        amount,
        paymentMethod, // "CARD" | "CASH" | ...
        paymentDetails, // string JSON
      })
    ),

  markSuccess: async ({ paymentId, referenceNumber }) =>
    dataOnly(
      await api.put(`/api/payments/${paymentId}/success`, { referenceNumber })
    ),

  markFailed: async (paymentId) =>
    dataOnly(await api.put(`/api/payments/${paymentId}/failed`)),

  getById: async (paymentId) =>
    dataOnly(await api.get(`/api/payments/${paymentId}`)),
};

/** ===================== QR PASS API =====================

 */
export const QrPassApi = {
  generate: async (reservationId) =>
    dataOnly(await api.post(`/api/qrpass/generate/${reservationId}`)),

  getByReservation: async (reservationId) =>
    dataOnly(await api.get(`/api/qrpass/reservation/${reservationId}`)),
};
