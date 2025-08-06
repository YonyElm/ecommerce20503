import { useEffect, useState, useContext } from "react";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import {activateUser, getUsers} from "../api/users";

export function UsersComponentContext() {
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();

  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    if (authContext.loading) {
      setLoading(false);
      return;
    }

    const user = authContext.user;
    if (!user || user.roleName !== "ADMIN") {
      navigate("/");
      setLoading(false);
      return;
    }

    getUsers(user.sub)
      .then((res) => {
        const users = res?.data?.users ?? [];
        const roleNames = res?.data?.roleNames ?? [];

        const usersJoinRole = users.map((user, i) => ({
          ...user,
          roleName: roleNames[i] ?? 'N/A',
        }));

        setUsers(usersJoinRole);
      })
      .catch((err) => {
        setUsers([])
        setError(err)
      })
      .finally(() => setLoading(false));
  }, [authContext.user, authContext.loading, navigate]);

  const handleActivateUser = async (targetUserId, action) => {
    let data = {}
    data["targetUserId"] = targetUserId
    data["action"] = action
    try {
      let result = await activateUser(authContext.user.sub, data);
      if (result.data.data != null) {
        setUsers(prev =>
          prev.map(user =>
            user.id === targetUserId ? { ...user, isActive: result.data.data } : user
          )
        );
      }
    } catch (err) {
      console.error("Failed to activate/deactivate user", err);
    }
  };

  return {
    users,
    loading,
    error,
    handleActivateUser,
  };
}