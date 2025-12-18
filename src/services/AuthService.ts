import ApiClient from "@/config/ApiClient";
import type LoginData from "@/models/LoginData";
import type LoginResponseData from "@/models/LoginResponseData";
import type { RegisterData } from "@/models/RegisterData";
import type User from "@/models/User";


// ================= REGISTER =================
export const registerData = async (signupData: RegisterData) => {
  const response = await ApiClient.post("/auth/register", signupData);

  return response.data;
};

// login function

export const loginUser= async (loginData: LoginData)=>{

  const response=await ApiClient.post<LoginResponseData>("/auth/login",loginData);
  return response.data;
};

//logout function

export const logoutUser= async ()=>{

  const response=await ApiClient.post<LoginResponseData>("/auth/logout");
  return response.data;
};

// get user by email 

export const getCurrentUser= async (emailId:string | undefined) => {

  const response=await ApiClient.get<User>(`/users/email/${emailId}`);
  return response.data;

}


