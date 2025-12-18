
import type LoginData from '@/models/LoginData';
import type LoginResponseData from '@/models/LoginResponseData';
import type User from '@/models/User';
import { loginUser, logoutUser } from '@/services/AuthService';
import { create } from 'zustand'
import { persist} from 'zustand/middleware'

const LOCAL_KEY= "auth_state";



type AuthState = {
   accessToken: string | null;
   user: User | null;
   authStatus: boolean;
   authLoading: boolean;
   login:  (loginData: LoginData) => Promise<LoginResponseData>;
   logout: (silent?: boolean) => void;
   checkLogin: () => boolean | undefined
}

// logic for global state
const useAuth = create<AuthState>()(
  persist(
    (set, get) => ({
      accessToken: null,
      user: null,
      authStatus: false,
      authLoading: false,

      login: async (loginData) => {
        console.log("started login...");
        set({ authLoading: true });

        try {
          const loginResponseData = await loginUser(loginData);
          console.log(loginResponseData);

          set({
            accessToken: loginResponseData.accessToken,
            user: loginResponseData.user,
            authStatus: true,
          });

          return loginResponseData;
        } catch (error) {
          console.log(error);
          throw error;
        } finally {
          set({
            authStatus: false,
          });
        }
      },

      logout: async (silent = false) => {
        try {
          set({
            authLoading: true,
          });
          await logoutUser();
        } catch (error) {
          
        } finally {
          set({
            authLoading: false,
          });
        }

        set({
          accessToken: null,
          user: null,
          authLoading: false,
          authStatus: false,
        });
      },

      checkLogin: () => {
        return !!get().accessToken;
      },
    }),
    {
      name: LOCAL_KEY,
      partialize: (state) => ({
        accessToken: state.accessToken,
        user: state.user,
      }),
    }
  )
);



export default useAuth;
