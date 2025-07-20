// src/theme.js (or wherever you define your theme)
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        // Primary Colors (Bright Cyan)
        primary: {
            main: '#00bcd4',
            light: '#e0f7fa',
            dark: '#0097a7',
            contrastText: '#fff',
        },
        // Secondary Colors (Steel Blue)
        secondary: {
            main: '#4a86b5',
            light: '#e0edf7',
            dark: '#3a6b90',
            contrastText: '#fff',
        },
        // Error Colors (Red)
        error: {
            main: '#ef4444',
            light: '#fecaca',
            dark: '#dc2626',
            contrastText: '#fff',
        },
        // Success Colors (Green)
        success: {
            main: '#10b981',
            light: '#d1fae5',
            dark: '#059669',
            contrastText: '#fff',
        },
        // Warning Colors (Amber)
        warning: {
            main: '#f59e0b',
            light: '#fef3c7',
            dark: '#d97706',
            contrastText: '#fff',
        },
        // Info Colors (Blue)
        info: {
            main: '#3b82f6',
            light: '#dbeafe',
            dark: '#2563eb',
            contrastText: '#fff',
        },
        // You can also define other palette properties like text, background, etc.
        text: {
            primary: '#1f2937',
            secondary: '#6b7280',
            disabled: '#9ca3af',
        },
        background: {
            default: '#f0f2f5',
            paper: '#fff',
        },
        // You can add custom colors outside the standard palette intentions if needed
        // For example, if you want 'header-dark' as a direct color:
        customColors: {
            headerDark: '#131921',
            borderLight: '#e5e7eb',
            headerNavLink: '#ccc',
            imagePlaceholder: '#e0e0e0',
            tableHeaderBg: '#f9fafb',
            tableRowEven: '#fcfcfc',
        },
    },
    // You might also want to customize typography, spacing, etc. here
    typography: {
        fontFamily: 'Inter, sans-serif',
    },
});

export default theme;