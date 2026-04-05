import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useOnlineStatus } from '@/hooks/use-online-status';
import { PwaUpdatePrompt } from '@/components/pwa-update-prompt';
import { useAuthStore } from '@/stores/auth-store';
import { RoleAwareRenderer } from '@/components/RoleAwareRenderer';
import { logout as apiLogout } from '@/lib/auth-api';

const NAV_ITEMS = [
  { path: '/patients', label: 'Pacientes', permission: 'patient:read' },
  { path: '/consultations', label: 'Consultas', permission: 'consultation:create' },
  { path: '/lab', label: 'Laboratorio', permission: 'lab:read' },
];

const ADMIN_ITEMS = [
  { path: '/admin/users', label: 'Usuarios', permission: 'user:manage' },
  { path: '/admin/roles', label: 'Roles', permission: 'role:manage' },
];

export function Layout() {
  const location = useLocation();
  const navigate = useNavigate();
  const connectionStatus = useOnlineStatus();
  const user = useAuthStore((s) => s.user);
  const activeBranch = useAuthStore((s) => s.activeBranch);
  const clearSession = useAuthStore((s) => s.clearSession);

  const statusDot = {
    online: 'bg-green-500',
    offline: 'bg-red-500',
    reconnecting: 'bg-amber-500',
  }[connectionStatus];

  async function handleLogout() {
    try {
      await apiLogout();
    } catch {
      // Best-effort server logout
    }
    clearSession();
    navigate('/login', { replace: true });
  }

  function isActive(path: string) {
    if (path === '/consultations') {
      return location.pathname === '/consultations' || location.pathname.includes('/consultation');
    }
    if (path === '/patients') {
      return location.pathname.startsWith('/patients') && !location.pathname.includes('/consultation');
    }
    return location.pathname.startsWith(path);
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 sticky top-0 z-30">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-14">
            <div className="flex items-center gap-6">
              <Link to="/" className="text-lg font-bold text-purple-700">SICEB</Link>
              <nav className="hidden sm:flex gap-1">
                {NAV_ITEMS.map((item) => (
                  <RoleAwareRenderer key={item.path} permission={item.permission}>
                    <Link
                      to={item.path}
                      className={`px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                        isActive(item.path)
                          ? 'bg-purple-100 text-purple-800'
                          : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900'
                      }`}
                    >
                      {item.label}
                    </Link>
                  </RoleAwareRenderer>
                ))}

                {ADMIN_ITEMS.map((item) => (
                  <RoleAwareRenderer key={item.path} permission={item.permission}>
                    <Link
                      to={item.path}
                      className={`px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                        isActive(item.path)
                          ? 'bg-amber-100 text-amber-800'
                          : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900'
                      }`}
                    >
                      {item.label}
                    </Link>
                  </RoleAwareRenderer>
                ))}
              </nav>
            </div>

            <div className="flex items-center gap-4">
              {activeBranch && (
                <span className="hidden sm:inline text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded">
                  {activeBranch.name}
                </span>
              )}
              <span className={`h-2 w-2 rounded-full ${statusDot}`} />
              {user && (
                <div className="flex items-center gap-2">
                  <span className="text-xs text-gray-600 hidden sm:inline">
                    {user.fullName}
                  </span>
                  <button
                    onClick={handleLogout}
                    className="text-xs text-gray-500 hover:text-red-600 transition-colors px-2 py-1 rounded hover:bg-red-50"
                  >
                    Salir
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <Outlet />
      </main>

      <PwaUpdatePrompt />
    </div>
  );
}
