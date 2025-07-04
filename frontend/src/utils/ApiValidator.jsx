

function isNumberString(input) {
    // Checks: not null, not empty, all digits, greater than 0
    return (
        typeof input === "string" &&
        input.trim() !== "" &&
        /^\d+$/.test(input) &&
        Number(input) > 0
    );
}

export { isNumberString };
