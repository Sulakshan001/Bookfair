import axios from "axios";

export default class Authentication {
  static BASE_URL = "http://localhost:8088";

  static async login(credentials) {
    const response = await axios.post(
      `${this.BASE_URL}/api/auth/login`,
      credentials
    );
    return response.data;
  }

  static logout() {
    localStorage.removeItem("token");
  }

  static isAuthenticated() {
    return !!localStorage.getItem("token");
  }
}
